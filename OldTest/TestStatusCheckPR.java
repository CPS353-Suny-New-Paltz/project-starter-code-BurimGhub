
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TestStatusCheckPR {
    
    private static final String COMPLETED = "completed";
    private static final int NUM_CHECKS = 2;
    private static final String SUCCESS = "success";
    private static final String APPROVED = "APPROVED";
    
    @Test
    public void testPullRequest() throws Exception {
        String baseApiPath = getBaseApiPath();
        String toCurl = baseApiPath + "pulls?state=all";
        String pullRequests = curl(toCurl);
        
        boolean foundPullRequest = false;
        // check each pull request to see if one meets assignment requirements
        for (JsonElement pr : JsonParser.parseString(pullRequests).getAsJsonArray().asList()) {
            String prNumber = pr.getAsJsonObject().get("number").getAsString();

            if (hasStatusChecks(baseApiPath, prNumber) &&
                    hasReviewerApproval(baseApiPath, prNumber)) {
                foundPullRequest = true;
                break;
            }
        }
        Assertions.assertTrue(foundPullRequest, "No pull request with required status checks (failure, then success) and reviewer approval found");
    }
    
    // query the git remote to find the repo URL
    private String getBaseApiPath() throws Exception {
        Process getRemote = new ProcessBuilder("git", "remote",  "get-url", "origin", "--push").start();
        getRemote.waitFor();
        String output = new String(getRemote.getInputStream().readAllBytes());
        String ownerRepo = output.substring("https://github.com/".length());
        int removeTrailingGit = ownerRepo.lastIndexOf(".");
        if (removeTrailingGit < 0) {
            removeTrailingGit = ownerRepo.length() - 1;
        }
        ownerRepo = ownerRepo.substring(0, removeTrailingGit);
        
        return "https://api.github.com/repos/" + ownerRepo + "/";
                
    }

    private boolean hasReviewerApproval(String baseApiPath, String prNumber) throws Exception {
        String getReviews = baseApiPath + "pulls/" + prNumber + "/reviews";
        String reviewResult = curl(getReviews);
        
        for (JsonElement review : JsonParser.parseString(reviewResult).getAsJsonArray().asList()) {
            if (review.getAsJsonObject().get("state").getAsString().equals(APPROVED)) {
                return true;
            }
        }
        return false;
    }

    // returns true if the PR has:
    // - two status checks
    // - both of which failed at some point
    // - both of which are passing now
    private boolean hasStatusChecks(String baseApiPath, String prNumber) throws Exception {
        String getCommits = baseApiPath + "pulls/" + prNumber + "/commits";
        String commitResult = curl(getCommits);
        
        List<JsonElement> commits = JsonParser.parseString(commitResult).getAsJsonArray().asList();
        if (commits.isEmpty()) { // weird, but don't crash
            return false;
        }
        sortCommits(commits);
        
        // check that the latest commit is successful
        JsonElement firstCommit = commits.get(0);
        Map<String, String> firstCommitStatus = getStatusCheckResult(baseApiPath, firstCommit);
       
        if (firstCommitStatus.size() != NUM_CHECKS) {
            return false;
        }
        for (String status : firstCommitStatus.values()) {
            if (!status.equals(SUCCESS)) {
                return false;
            }
        }
        
        // check that an earlier commit failed
        Set<String> failuresFound = new HashSet<>();
        for (JsonElement commit : commits) {
            Map<String, String> statusCheckResult = getStatusCheckResult(baseApiPath, commit);
            statusCheckResult.forEach((check, status) -> {
                if (!status.equals(SUCCESS)) {
                    failuresFound.add(check);
                }
            });
        }
        
        return failuresFound.size() == NUM_CHECKS;
    }

    // sort commits by date, newest first
    private void sortCommits(List<JsonElement> commits) {
        Collections.sort(commits, (c1, c2) -> {
            try {
                return -1*getCommitDate(c1).compareTo(getCommitDate(c2));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // parse commit date from the json
    private Date getCommitDate(JsonElement c1) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(c1.getAsJsonObject().get("commit").getAsJsonObject().get("committer").getAsJsonObject().get("date").getAsString());
    }

    // parse check names and results from the json
    private Map<String, String> getStatusCheckResult(String baseApiPath, JsonElement commit) throws Exception {
        String sha = commit.getAsJsonObject().get("sha").getAsString();
        String getStatusChecks = baseApiPath + "commits/" + sha + "/check-runs";
        String statusCheckResult = curl(getStatusChecks);
        Map<String, String> checkToStatus = new HashMap<>();

        for (JsonElement check : JsonParser.parseString(statusCheckResult).getAsJsonObject().get("check_runs").getAsJsonArray().asList()) {
            String name = check.getAsJsonObject().get("name").getAsString();
            String status =  check.getAsJsonObject().get("status").getAsString();
            if (status.equals(COMPLETED)) {
                String result = check.getAsJsonObject().get("conclusion").getAsString();
                checkToStatus.put(name, result);
            }
        }
        
        return checkToStatus;
    }

    private String curl(String toCurl) throws Exception {
        URL url = new URI(toCurl).toURL();

        String result = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            String line; 
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
        }
        return result;
    }
}
