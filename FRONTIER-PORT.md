# Frontier Paper 26.4 Snapshot 8

Experimental independent port based on Paper commit 806b0db07c2c625f80cf47a1f6cab4d1735d1b8e. It is not an official Paper release.

This repository captures the 26.4 Snapshot 8 Paper source and the related modified Mache source in frontier-mache/. The Bloom test server has run this snapshot, but this does not establish production stability or complete client compatibility.

## Build requirements and limitations

Java 25 is required. The Paper build refers to the locally published io.papermc:mache:26.4-snapshot-8+build.local-SNAPSHOT artifact. The Mache source is included to preserve the build inputs; the steps for building and publishing it on a clean machine still need independent verification. The snapshot Paper patch chain and dependency downloads likewise need a clean-room build before this repository can be considered reproducible.

The source is a snapshot of the local build checkout; generated or ignored build files, Minecraft server jars, world data, secrets, and server configuration are excluded. FrontierBridge and gameplay plugins are separate projects. Do not run historical migration scripts against this checkout without checking their prerequisites.

Retain upstream copyright and license notices; see LICENSE.md and frontier-mache/license.txt.
