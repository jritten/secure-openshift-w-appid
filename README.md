# Tutorial: Application Security

This tutorial explores modern user and microservice security. Specifically, this tutorial enables the following:
* Separate UI deployment unit
* Leverages `Backend-for-frontend` pattern
* Leverages externalize Authentication services based on `OpenID Connect` (OIDC)
* Microservice security leverages OIDC `Java Web KeySet (JWKS)` Public Keys

*NOTE:* The monolith application is based on the [Spring Security and Angular Tutorial](https://spring.io/guides/tutorials/spring-security-and-angular-js/).


## Table of Contents
* [Pre-reqs](#pre-reqs)
* [Part 1: Running local](#part-1-running-local)
* [Part 2: Run on OpenShift](#part-2-run-on-openshift)

## Pre-reqs

### A. Red Hat CodeReady Container
1. [Install on Laptop: Red Hat CodeReady Containers](https://cloud.redhat.com/openshift/install/crc/installer-provisioned)

    **NOTES:**

    a. Requires Red Hat Developer credentials, which gives you access to developer tools and programs via [Red Hat Developer](https://developers.redhat.com). To sign-up, click the `Log In` link on the top right of the page and then click the `Create one now` link.


    b. Perform [Chapter 2. Installation](https://access.redhat.com/documentation/en-us/red_hat_codeready_containers/1.8/html-single/getting_started_guide/index#installation_gsg)

1. Confirm OpenShift CRC `running` on the local machine:

    ```sh
    crc status
    ```

### B. IBM App ID
1. You must have an IBM Cloud account. If you don't have one, [sign up for a trial](https://cloud.ibm.com/registration). The account requires an `IBMid`. If you don't have an `IBMid`, you can create one when you register.

1. Setup `App ID`, the IBM OpenID Connect (OIDC) service
    1. Create `App ID`
        1. Open Browser to [Create AppID](https://cloud.ibm.com/catalog/services/app-id)
        1. Under `Select a region`, confirm or select `Dallas` and click the `Create` button
    1. Setup `Cloud Identity Provider`
        1. Within App ID page, click `Manage Authentication` and perform the following:
            1. `Disable` the following Identity Providers: `Facebook, Google, Anonymous`
            1. Confirm the following is `enabled`: `Cloud Directory`
    1. Add `web redirect URLs`
        1. Within App ID page, click `Manage Authentication` -> `Authentication Settings`. Under `Add web redirect URLs`, add the following URLs:
            1. Add `http://localhost:8080/login` and click the `Plus` button
            1. Add `http://modern-bff-cloudready-security.apps-crc.testing/login` and click the `Plus` button
    1. Setup `Cloud Directory`:
        1. Within App ID page, click `Cloud Directory` -> `Settings`. Under `Allow users to sign-up and sign-in using:` select `Username and password`
        1. Within App ID page, click `Cloud Directory` -> `Users`. Click `Create User`, complete form and click `Save`

            **IMPORTANT:** Note `username` and `password` for later use

    1. Add OIDC client app
        1. Within the App ID page, click `Applications`  and click `App Application`.
        1. Enter and select the following:
            Name: `gm4cappmod-app`
            Type: `Regular web application`
        1. Click `Save`
        1. Expand `gm4cappmod-app` and note the following for later use
            * `clientId`
            * `tenantId`
            * `secret`
    
### C. Git Command Line Tools
1. **Optional:** If you do not have Git client installed, go to the [Git client downloads](https://git-scm.com/downloads) website to download and install the Git client.

    **NOTE (Windows Users):** During install, on the `Choosing HTTPS transport backend` step, select `Use the native Windows Secure Channel library`.

### D. IBM Cloud Gitlab SSH Keys setup

1. [Generating a new SSH key pair](https://us-south.git.cloud.ibm.com/help/ssh/README#generating-a-new-ssh-key-pair).
   **NOTE:** leverage RSA instructions


1. [Adding an SSH key to your GitLab account](https://us-south.git.cloud.ibm.com/help/ssh/README#adding-an-ssh-key-to-your-gitlab-account)


### E. (Optional) Visual Studio Code

1. **Optional** If you do not have Visual Studio Code installed, go to the [Visual Studio Code Download](https://code.visualstudio.com/download) website to download and install the Visual Studio Code.

1. Install Visual Studio Code Extensions:

    * [Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)
    * [Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=Pivotal.vscode-boot-dev-pack)


### F. (Optional - Windows) Java Development Kit installation

1. Verify Java Installation with the following command:

    ```sh
    java -fullversion
    ```

1. **Optional:** If you do not have Java installed, go to the [Java SE Development Kit 8 Downloads](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) website to download and install Java.

     **NOTE:** During installation, change installation to **c:\\** ( e.g. C:\jdk1.8.0_231 )

1. Add `JAVA_HOME` system environment variable that point to your base java folder path (e.g., C:\jdk1.8.0_231 )


### G. (Optional) NodeJS Installation

1. Verify NodeJS Installation with the following command:

    ```sh
    npm -version
    ```
    
    **Optional:** If you do not have NodeJS installed, go to the [NodeJS](https://nodejs.org/en/) website to download and install NodeJS.

### H. Clone cloudready-app-security

1. Clone **cloudready-app-security.git** GIT repo

    ```sh
    git clone git@us-south.git.cloud.ibm.com:gm4c-mod/cloudready-app-security.git
    ```


## Part 1: Running locally

01. Open `cloudready-app-security` inside Visual Studio Code

     1. Open Visual Studio Code
     1. File->Open Workspace... -> cloudready-app-security.code-workspace

01. Update `REPLACE_WITH_CLIENT_ID`, `REPLACE_WITH_TENANT_ID` and `REPLACE_WITH__SECRET` with registered `App ID` app
    
    1. With Visual Studio Code, click menu bar `Edit`-> `Replace in Files`
    1. For `Search` field, enter `REPLACE_WITH_CLIENT_ID`
    1. For `Replace` field, enter `clientId` data saved from [B. IBM App ID](#B-ibm-app-id) step 5
    1. With Visual Studio Code, click menu bar `Edit`-> `Replace in Files`
    1. For `Search` field, enter `REPLACE_WITH_TENANT_ID`
    1. For `Replace` field, enter `tenantId` data saved from [B. IBM App ID](#B-ibm-app-id) step 5
    1. With Visual Studio Code, click menu bar `Edit`-> `Replace in Files`
    1. For `Search` field, enter `REPLACE_WITH__SECRET`
    1. For `Replace` field, enter `secret` data saved from [B. IBM App ID](#B-ibm-app-id) step 5

01. Run **resource-ms** locally. Within Visual Studio Code:
    1. Expand **SPRING-BOOT DASHBOARD**, click the refresh icon, right mouse click on **resource-ms** and click **Start**

01. Run **modern-bff** locally. Within Visual Studio Code:
    1. Expand **SPRING-BOOT DASHBOARD**, click the refresh icon, right mouse click on **modern-bff** and click **Start**


01. Run **modern-ui** locally. Within Visual Studio Code:
    
    1. Install `modern-ui` dependent npm modules
    
       1. Select `Terminal` from menu bar, then `Run Task...` and select `npm: install:all - modern-ui`

          **NOTE**: Check Terminal and enter an appropriate response to `Would you like to share anonymous usage data with the Angular Team at Google ...` question, if prompted

    1. Build `modern-ui` angular resources:
       
       1. Select `Terminal` from the menu bar, then `Run Task...` and select `npm: build - modern-ui` and select `Continue without scanning the task output`
     
    1. Run `modern-ui` node app:

       1. Select `Terminal` from the menu bar, then `Run Task...` and select `npm: start - modern-ui` and select `Continue without scanning the task output`

01. Open browser to http://localhost:4200

01. Click **Login**, enter your username and password and click submit:

01. Click [Details](http://localhost:4200/details). You should observe ID and content information


## Part 2: Run on OpenShift

01. Within Visual Studio Code Terminal, create an OpenShift project as the `developer` user
    **NOTE:** Verify user via `oc whoami`
    
    **macOS/Unix**

    ```sh
    eval $(crc oc-env); oc login -u developer
    oc new-project cloudready-security
    ```
    
    **Windows**

    ```sh
     & crc oc-env | Invoke-Expression
     oc login -u developer
     oc new-project cloudready-security
    ```

01. Deploy **resource-ms** microservice application to OpenShift

     01. Build and deploy **resource-ms** microservice to OpenShift. Within Visual Studio, perform the following within Terminal:

         ```sh
         cd resource-ms
         
         # Create your OpenShift resource descriptors
         # Build component and start S2I build
         # and Deploy your microservice on Openshift cluster
         ./mvnw clean package oc:deploy -Popenshift
         ```

          **NOTE:** 
          * Ignore `ERROR`s denoting cannot extract Git information
          * When deploying new code or configurations changes, perform the following after running `oc:deploy`
            
            ```sh
            oc rollout latest resource-ms
            ```

     01. Check the status of `resource-ms` app

          ```sh
          oc status
          ```

          **NOTE:** Might need to repeat the command until it displays `deployment #1 running`


01. Deploy **modern-bff** microservice application to OpenShift

     01. Build and deploy **modern-bff** microservice to OpenShift. Within Visual Studio, perform the following within Terminal:
     
         ```sh
         cd ../modern-bff
         
         # Create your OpenShift resource descriptors
         # Build component and start S2I build
         # and Deploy your microservice on Openshift cluster
         ./mvnw clean package oc:deploy -Popenshift
         ```
         
          **NOTE:** 
          * Ignore `ERROR`s denoting cannot extract Git information
          * When deploying new code or configurations changes, perform the following after running `oc:deploy`
            
            ```sh
            oc rollout latest modern-bff
            ```         

     01. Check the status of `modern-bff` app

          ```sh
          oc status
          ```

          **NOTE:** Might need to repeat the command until it displays `deployment #1 running`

01. Deploy **modern-ui** microservice UI application to OpenShift

     01. Build and deploy **modern-ui** microservice to OpenShift. Within Visual Studio, perform the following within Terminal:

         **MacOS**

         ```sh
         cd ../modern-ui
         npm run build
         npm run nodeshift
         oc expose svc/modern-ui
         ```

     01. Check the status of `modern-ui` app

          ```sh
          oc status
          ```

          **NOTE:** Might need to repeat the command until it displays `deployment #1 deployed`


01. Open browser to `modern-ui`

    1. Open browser to http://modern-ui-cloudready-security.apps-crc.testing
     
    1. Click **Login**, enter the following and click submit:

       username: <your username>
       password: <your password>

    1. Click `Details`. You should observe ID and content information