package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.BuildConfig;

public class TeamVersion {

    public final String gitCommit;
    public final String gitBranch;
    public final String buildDate;
    public final boolean gitDirty;
    public final String builder;
    public final String sdkVersion;
    private final String formatted;
    private final String formattedVersion;
    private final String formattedBuild;

    public static final TeamVersion VERSION = new TeamVersion(
            BuildConfig.GIT_COMMIT, BuildConfig.GIT_BRANCH, BuildConfig.BUILD_DATE,
            BuildConfig.GIT_DIRTY, BuildConfig.BUILDER, BuildConfig.VERSION_NAME);

    public TeamVersion(String gitCommit, String gitBranch, String buildDate,
                       boolean gitDirty, String builder, String sdkVersion) {
        this.gitCommit = gitCommit;
        this.gitBranch = gitBranch;
        this.buildDate = buildDate;
        this.gitDirty = gitDirty;
        this.builder = builder;
        this.sdkVersion = sdkVersion;
        String displayDate = buildDate.replace('T', ' ');
        if (displayDate.length() > 19) displayDate = displayDate.substring(0, 19);
        this.formattedVersion = String.format("%s-%s%s", gitBranch, gitCommit, (gitDirty ? "-dirty" : ""));
        this.formattedBuild = String.format("%s - %s", displayDate, builder);
        this.formatted = formattedVersion + " (" + formattedBuild + ")";
    }


    public static TeamVersion getVersion() {
        return VERSION;
    }

    public String getFormattedVersion() {
        return formattedVersion;
    }

    public String getFormattedBuild() {
        return formattedBuild;
    }

    @Override
    @NonNull
    public String toString() {
        return formatted;
    }
}