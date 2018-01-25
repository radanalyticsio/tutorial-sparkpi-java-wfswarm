# wfswarm-sparkpi
A Java implementation of SparkPi using [WildFly Swarm](http://wildfly-swarm.io/)

This application is an example tutorial for the
[radanalytics.io](http://radanalytics.io) community. It is intended to be
used as a source-to-image (s2i) application.

## Quick start

You should have access to an OpenShift cluster and be logged in with the
`oc` command line tool.

1. Create the necessary infrastructure objects
   ```bash
   oc create -f http://radanalytics.io/resources.yaml
   ```

1. Launch wfswarm-sparkpi
   ```bash
   oc new-app --template oshinko-java-spark-build-dc \
       -p APPLICATION_NAME=wfswarm-sparkpi \
       -p GIT_URI=https://github.com/radanalyticsio/tutorial-sparkpi-java-wfswarm \
       -p APP_FILE=wfswarm-sparkpi-1.0-SNAPSHOT-swarm.jar
   ```

1. Expose an external route
   ```bash
   oc expose svc/wfswarm-sparkpi
   ```

1. Visit the exposed URL with your browser or other HTTP tool, for example:
   ```bash
   $ curl http://`oc get routes/wfswarm-sparkpi --template='{{.spec.host}}'`/app
   WildFly Swarm SparkPi server ready.
   Add /sparkpi to invoke Pi computation.

   $ curl http://`oc get routes/wfswarm-sparkpi --template='{{.spec.host}}'`/app/sparkpi
   Pi is roughly 3.140480
   ```

### Optional parameter

If you would like to change the number of samples that are used to calculate
Pi, you can specify them by adding the `scale` argument to your request
, for example:

```bash
$ curl http://`oc get routes/wfswarm-sparkpi --template='{{.spec.host}}'`/sparkpi?scale=10
Pi is roughly 3.141749
```
