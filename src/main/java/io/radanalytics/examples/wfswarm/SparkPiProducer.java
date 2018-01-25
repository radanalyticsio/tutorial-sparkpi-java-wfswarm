package io.radanalytics.examples.wfswarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

// make this available in CDI as a bean so we can look it up
@Model
@ApplicationScoped
public class SparkPiProducer implements Serializable {

    private SparkConf sparkConf;
    private JavaSparkContext javaSparkContext;

    // we want to read the name and path of our Spark app jar
    // from external CDI configuration that has been partially
    // populated by Maven as a filtered resource
    @Inject
    @ConfigurationValue("spark.submit.jarfile")
    String appJarFile;

    public SparkPiProducer() {}

    // use CDI events to "force" early initialization of our Spark environment
    // as part of the deployment in WildFly Swarm
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        this.sparkConf = new SparkConf().setAppName("WildFly Swarm SparkPi");
        this.sparkConf.setJars(new String[]{appJarFile});
        this.javaSparkContext = new JavaSparkContext(sparkConf);
    }

    public String getPi(int scale) {
        // should get something here from the Resource query
        // but just in case we are called from elsewhere...
        int slices = scale>1 ? scale : 2;
        int n = 100000 * slices;
        List<Integer> l = new ArrayList<Integer>(n);
        for (int i = 0; i < n; i++) {
            l.add(i);
        }

        JavaRDD<Integer> dataSet = javaSparkContext.parallelize(l, slices);

        int count = dataSet.map(integer -> {
            double x = Math.random() * 2 - 1;
            double y = Math.random() * 2 - 1;
            return (x * x + y * y < 1) ? 1 : 0;
        }).reduce((integer, integer2) -> integer + integer2);

        String ret = "Pi is rouuuughly " + 4.0 * count / n;

        return ret;
    }
}
