# Visual testing automation

Sources for the [blog article](http://balamaci.ro/visual-testing-automation/) in which we propose a solution for the automatization of visual testing a web application.

We build upon the previous [article]() of **deploying a .war file of a web application inside a generic Tomcat web server 
deployed into a docker container** and running integration tests all of this as steps of a maven project.

We propose on **using WebDriver capabilities to take printscreens of key points in the application** and use those as 
reference. 
We use the **@RunWith(Parameterized.class)** option to pass in **different resolutions to cover a wide range of screen sizes**. 

On **subsequent runs of the same tests we take new printscreens**. We match the new printscreens against the reference ones.
We **compare the hash values of the image content** against the references and if they are different we **highlight the diffs**.  
We use ImageMagick's external library(ran through **im4java** wrapper library) to get the diff image from the reference.

I've used as example the AngularJs demo web app of the [pippo](https://github.com/decebals/pippo) framework.
**pippo-demo.war** our web app that we'll be testing is already present inside the project as to be easier to try out the demo. 
In our real life scenario the war file would be produced by Jenkins in a separate job.

## Prerequisites to running yourself locally  
  
  - Docker 
  - phantomjs - which in turn needs **nodejs** and **npm**
      We already talked about installing them in [here](http://balamaci.ro/continous-integration-with-jenkins-docker-ansible-webdriver/#webdriver). Basically just run a "npm install phantomjs".
  - Imagemagick - helps us with obtaining a "diff" image between the reference sprintscreens and the new printscreens. 
      Installs as simple as "apt-get install imagemagick"  

## Running the tests
After all the prerequisites have been met you can run:
```bash  
  mvn -DflagUpdateReferenceScreenshots=true clean verify
```   
to run the tests and generate the reference printscreens.

to run the tests output **.diff.png** image files and fail the tests on different images:
```bash  
  mvn clean verify 
```  