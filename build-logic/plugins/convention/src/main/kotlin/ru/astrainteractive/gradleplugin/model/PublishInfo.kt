package ru.astrainteractive.gradleplugin.model

data class PublishInfo(
    val libraryName: String,
    val description: String,
    val gitHubOrganization: String,
    val gitHubName: String,
    val license: String,
    val publishGroupId: String,
    val ossrhUsername: String,
    val ossrhPassword: String,
    val signingKeyId: String,
    val signingPassword: String,
    val signingKey: String
) {
    val gitHubUrl = "https://github.com/$gitHubOrganization/$gitHubName"
    val sshUrl = "scm:git:ssh://github.com/$gitHubOrganization/$gitHubName.git"
}
