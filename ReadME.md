# Percy-sample-demo

A demo test to showcase the power of Percy!


## Requirements
- Automate Access
- Set the environment variables or pass the user and access key in the yml file
- Set the Full access PERCY TOKEN in the .env file in the root directory
- To set the env vairables run the below commands
- For \*nix based and Mac machines:

```sh
export BROWSERSTACK_USERNAME=<browserstack-username> &&
export BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>
```

  - For Windows:

```shell
set BROWSERSTACK_USERNAME=<browserstack-username>
set BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>
```

## Running Your Tests
- To runs needs to be executed for comparing Pre Prod vs Prod
- The first run should include the Pre-Prod urls and the second run with prod urls

### Run a specific test on BrowserStack

In this section, we will run three tests on Chrome browser on Browserstack. Each test will be executed on each brand.

- How to run the test?

  To run the default test scenario, use the following command:

- On Pre-Prod
   Set the below environment variables
   For \*nix based and Mac machines:

  ```sh
  export PERCY_BRANCH=preprod
  ```

  For Windows:
  ```shell
  set PERCY_BRANCH=preprod
  ```  

    Run the npm command as below
        ```sh
        npm run pre-prod-test
        ```
- On Prod
   Set the below environment variables
   For \*nix based and Mac machines:

   ```sh
   export PERCY_BRANCH=prod
   export PERCY_TARGET_BRANCH=preprod
   ```

  For Windows:
  ```shell
  set PERCY_BRANCH=prod
  set PERCY__TARGET_BRANCH=preprod
  ```  
  Run the npm command as below
    ```sh
    npm run prod-test
    ```  
- you can view the results in the percy dashboard - https://percy.io/dashboard  
## Known issues

- The pre-prod urls in pre-prod uses bot detection and hence the screenshots for the actual page is not showing up
  The prod urls are showing access denied. Could be because of bot detection too.