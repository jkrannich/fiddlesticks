# Releasing Fiddlesticks

The repository publishes two libraries from the same release tag:

```text
io.github.jkrannich:leagueapi:<version>
io.github.jkrannich:datadragon:<version>
```

The default development version is `0.1.0-SNAPSHOT`. A release version is supplied explicitly so a release cannot happen accidentally from a normal build.

## 1. Create a Central Portal token

Log in to Central Portal and open the [user tokens page](https://central.sonatype.com/usertoken). Click **Generate User Token**, choose a display name and expiration, then save the generated username/password immediately. These are publishing-token credentials, not your normal Central Portal login password. Central does not show the token password again after the dialog closes. Do not put the token in the repository. The token username and password are supplied as:

```text
CENTRAL_PORTAL_USERNAME
CENTRAL_PORTAL_PASSWORD
```

## 2. Create a GPG signing key

On Windows, install [Gpg4win](https://gpg4win.org/), open a new PowerShell window, and verify it:

```text
gpg --version
```

Create a key pair:

```text
gpg --full-generate-key
gpg --list-secret-keys --keyid-format=long
```

When prompted, use your name and an email address you control, select RSA, and protect the key with a strong passphrase. The output contains a `sec rsa.../KEY_ID` line. Use the ID after `sec`, which is the primary key ID; do not use an `ssb` subkey ID.

Publish only the public key to a supported keyserver:

```text
$primaryKeyId = 'YOUR_PRIMARY_KEY_ID'
gpg --keyserver keyserver.ubuntu.com --send-keys $primaryKeyId
```

Central's full requirements are documented [here](https://central.sonatype.org/publish/requirements/gpg/). Export the private key outside the repository and store the result only as a CI secret:

```text
$signingKeyFile = Join-Path $env:TEMP "fiddlesticks-signing-key.asc"
$primaryKeyId = 'YOUR_PRIMARY_KEY_ID'
gpg --armor --export-secret-keys $primaryKeyId | Set-Content -Path $signingKeyFile -Encoding ascii
```

Copy the contents of that temporary file into the `GPG_PRIVATE_KEY` GitHub secret, then delete the temporary file.

The build reads these Gradle properties from CI environment variables:

```text
ORG_GRADLE_PROJECT_signingKey
ORG_GRADLE_PROJECT_signingKeyId
ORG_GRADLE_PROJECT_signingPassword
```

Never commit `signing-key.asc`, a private key, or a passphrase.

## 3. Test locally

Normal development build:

```text
gradlew :leagueapi:test :datadragon:test :leagueapi:build :datadragon:build
```

Local consumer publication:

```text
gradlew :leagueapi:publishToMavenLocal :datadragon:publishToMavenLocal
```

Use this dependency from another local project:

```kotlin
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("io.github.jkrannich:leagueapi:0.1.0-SNAPSHOT")
    implementation("io.github.jkrannich:datadragon:0.1.0-SNAPSHOT")
}
```

## 4. Publish a release

Create and push a new version tag. The existing `v0.2.0` release is immutable, so use the next version appropriate for the changes, such as `v0.3.0`. GitHub Actions will validate both modules, sign both publications, upload them together to the Central Portal, and create a draft GitHub Release with each JAR, sources, and Javadoc:

```text
git tag v0.3.0
git push origin v0.3.0
```

The repository is configured with `USER_MANAGED`, so the workflow uploads the Central deployment for validation but does not publish it automatically. Open the deployment in Central Portal and click **Publish** after validation. Central versions are immutable; use a new version for corrections.

The workflow then creates a **draft GitHub Release**. A draft is an editable, private-to-collaborators release record attached to the pushed tag. After Central releases the component, open GitHub's **Releases** page, review the generated notes and assets, and click **Publish release**. This makes the GitHub release public.

The manual equivalent is:

```text
gradlew :leagueapi:verifyReleaseConfiguration :datadragon:verifyReleaseConfiguration publishAggregationToCentralPortal '-PreleaseVersion=0.3.0'
```

Set the five secret-backed environment variables before running that command.
