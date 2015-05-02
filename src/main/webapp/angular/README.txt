angular-bootstrap
===========
AngularJS and Twitter bootstrap combined
    AngularJS `http://angularjs.org/`
    Twitter bootstrap `http://getbootstrap.com/`
    
## Installation and running tasks

Install [node.js](http://nodejs.org/) then navigate to the site root within terminal and type:

    npm install

Once the node modules have installed you should now have access to run the task runner. In your terminal you should be able to use the following commands:

    gulp docs
    gulp test
    gulp compile
    gulp watch
    
If you are wanting to add/change icons, you can find a nice visual list here:

    http://glyphicons.bootstrapcheatsheets.com
    
## Running the app in Eclipse during development

1) Create HTTP review server
  File->new->other->server
  Basic->HTTP Preview->Finish
2) Start HTTP server on Servers bookmark 
  ...wait until the server start
3) Open html file
  src\main\webapp\angular\app\index.html
  swith to bookmark Preview
  
    
## Running unit tests

We are using [jasmine](http://pivotal.github.com/jasmine/) and
[Karma](http://karma-runner.github.io) for unit tests.

The task runner requires [node.js](http://nodejs.org/), [Grunt](http://gruntjs.com/), [Karma](http://karma-runner.github.io/) and a browser. To run the test run the following command:

    gulp test

## Directory Layout

    app/                --> all of the files to be used in production
      data/             --> json data files
      index.html        --> app layout file (the main html template file of the app)
      libs/             --> external libraries and fonts
      modules/          --> modules grouped by functionality
        app/            --> main application module
        item/           --> view/edit item
        item-new/       --> new item
        items/          --> list of items
        overlay/        --> popup overlay
    test/               --> test source files and libraries
      e2e/              --> end-to-end test runner (open in your browser to run)
      unit/             --> unit level specs/tests
    build/              --> Auto generated minified code after running gulp compile
    docs/               --> Auto generated documentation after running gulp docs

## Contact

For more information on AngularJS please check out `http://angularjs.org/`

## Troubleshooting

# Eclipse building workspace hangs after importing existing maven project because of JavaScript validation
1)Kill Eclipse process.
2)Go to project folder and edit .project file.
3)Remove the following lines (it will disable failing JavaScript validator):

<buildCommand>
  <name>org.eclipse.wst.jsdt.core.javascriptValidator</name>
  <arguments>
  </arguments>
</buildCommand>

4)Save file.
5)Re-open Eclipse.
