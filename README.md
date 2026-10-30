OpenDID TA SDK Server
==

Welcome to the OpenDID TA SDK Server Repository. <br>
This repository contains the SDK source code, documentation, and related resources for implementing the Trust Agent (TAS) server functionality in the OpenDID ecosystem.

## Folder Structure
An overview of the main folders and documents in the project directory:

```
did-ta-sdk-server
├── CHANGELOG.md
├── CLA.md
├── CODE_OF_CONDUCT.md
├── CONTRIBUTING.md
├── LICENSE
├── dependencies-license.md
├── MAINTAINERS.md
├── README.md
├── README_ko.md
├── RELEASE-PROCESS.md
├── SECURITY.md
├── docs
│   └── api
│       └── TA_SDK-SERVER_API_ko.md
│       └── TA_SDK-SERVER_API.md
│   └── errorCode
│       └── TA_ErrorCode.md
└── source
    └── did-ta-sdk-server
        ├── gradle
        ├── libs
        ├── src
        ├── build.gradle
        └── README.md
```

<br/>

Descriptions for each folder and file are as follows:

| Name                               | Description                                              |
|----------------------------------|----------------------------------------------------------|
| CHANGELOG.md                     | Version-by-version changes for the project               |
| CODE_OF_CONDUCT.md               | Contributor code of conduct                              |
| CONTRIBUTING.md                  | Contribution guidelines and procedures                   |
| LICENSE                          | License                                                  |
| dependencies-license.md          | License information for project dependency libraries     |
| MAINTAINERS.md                   | Project maintainer guidelines                            |
| RELEASE-PROCESS.md               | Procedures for releasing new versions                    |
| SECURITY.md                      | Security policy and vulnerability reporting methods      |
| docs                             | Documentation                                            |
| ┖ api                            | API guide documents                                      |
| ┖ errorCode                      | Error codes and troubleshooting guides                   |
| source                           | SDK source code project                                  |
| ┖ did-ta-sdk-server              | TA SDK source code and build files                       |
| &nbsp;&nbsp;&nbsp;┖ gradle       | Gradle build configuration and scripts                   |
| &nbsp;&nbsp;&nbsp;┖ libs         | OmniOne SDK JAR files                                    |
| &nbsp;&nbsp;&nbsp;┖ src          | Main source code directory                               |
| &nbsp;&nbsp;&nbsp;┖ build.gradle | Gradle build configuration file                          |
| &nbsp;&nbsp;&nbsp;┖ README.md    | Source code overview and instructions                    |

<br/>

## Libraries

The libraries used in this project are organized into two main categories.

1. **Third-party Libraries**: These are open source dependencies managed through the [build.gradle](source/did-ta-sdk-server/build.gradle) file. For a detailed list of third-party libraries and their licenses, please refer to the [dependencies-license.md](dependencies-license.md) file.

2. **OmniOne SDK**: Libraries included in the `source/did-ta-sdk-server/libs` folder, which are local dependencies from the OmniOne DID project.

## API Reference

For detailed information about the API, please refer to the [TA SDK Server API](./docs/api/TA_SDK-SERVER_API_ko.md) documentation.

## Change Log

The Change Log records detailed changes and updates for each version. You can find it here:
- [Change Log](CHANGELOG.md)

## OpenDID Demo Video
To watch a demonstration video of the OpenDID system, please visit the [Demo Repository](https://github.com/OmniOneID/did-demo-server). <br>

This video demonstrates key features including user registration, VC issuance, and VP submission processes.

## Contributing

For details on contribution procedures and the code of conduct, please refer to [CONTRIBUTING.md](CONTRIBUTING.md) and [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md).

## License
[Apache 2.0](LICENSE)
