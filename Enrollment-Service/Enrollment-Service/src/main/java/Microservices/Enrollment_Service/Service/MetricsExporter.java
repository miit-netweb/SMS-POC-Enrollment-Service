package Microservices.Enrollment_Service.Service;

import java.sql.Timestamp;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import Microservices.Enrollment_Service.Entity.ApplicationMetric;
import Microservices.Enrollment_Service.Repository.ApplicationMetricRepository;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class MetricsExporter {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private ApplicationMetricRepository metricRepository;
    
    Logger LOGGER=LoggerFactory.getLogger(MetricsExporter.class);

    @Scheduled(fixedRate = 120000)
    public void exportMetrics() {
        Collection<Meter> meters = meterRegistry.getMeters();

        for (Meter meter : meters) {
            meter.measure().forEach(measurement -> {
                String metricName = meter.getId().getName();
                double metricValue = measurement.getValue();
                ApplicationMetric metric = new ApplicationMetric();
                metric.setMetricName(metricName);
                metric.setMetricValue(metricValue);
                metric.setTimestamp(new Timestamp(System.currentTimeMillis()));
                //LOGGER.info("Metrics to be inseted:{}",metric);
                metricRepository.save(metric);
               // LOGGER.info("Metrics Inserted");
            });
        }
    }
}

