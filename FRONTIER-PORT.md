# Frontier Paper 26.4 Snapshot 8

Experimental independent port based on Paper commit 806b0db07c2c625f80cf47a1f6cab4d1735d1b8e. It is not an official Paper release.

This repository captures the 26.4 Snapshot 8 Paper source and the related modified Mache source in frontier-mache/. The Bloom test server has run this snapshot, but this does not establish production stability or complete client compatibility.

## Build requirements and limitations

Java 25 is required. The Paper build refers to the locally published io.papermc:mache:26.4-snapshot-8+build.local-SNAPSHOT artifact. The Mache source is included to preserve the build inputs; the included source can publish this artifact to Maven local. A clean-checkout build with initially empty Gradle and Maven caches succeeded on Windows; an independent machine has not yet been tested.

The source is a snapshot of the local build checkout; generated or ignored build files, Minecraft server jars, world data, secrets, and server configuration are excluded. FrontierBridge and gameplay plugins are separate projects. Do not run historical migration scripts against this checkout without checking their prerequisites.

Retain upstream copyright and license notices; see LICENSE.md and frontier-mache/license.txt.

## Clean build procedure

Use a Git clone and JDK 25. Build and publish the included Mache version first,
then generate the Paper sources before creating the executable jar:

```powershell
cd frontier-mache
./gradlew.bat ':versions:v26_4-snapshot-8:publishMachePublicationToMavenLocal'
cd ..
./gradlew.bat applyPatches --no-configuration-cache
./gradlew.bat createPaperclipJar --no-configuration-cache
```

A Windows clean-checkout build with initially empty dependency caches completed
Mache publication, applyPatches, and createPaperclipJar on 2026-11-12. The
resulting jar is under paper-server/build/libs/. This was an isolated build on
the existing computer, not an independent machine. Network access is required
for Minecraft and dependencies.

On Windows use a very short physical path such as C:/fp: nested upstream builds
failed from longer paths even with core.longpaths enabled. Enable Git long paths
and provide a Git identity for generated source repositories. The build did not
use Minecraft worlds, plugins, or credentials from the live server.
