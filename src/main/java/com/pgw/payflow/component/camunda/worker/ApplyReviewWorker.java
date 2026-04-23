package com.pgw.payflow.component.camunda.worker;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component("applyReviewDelegate")
public class ApplyReviewWorker implements JavaDelegate {
  @Override
  public void execute(DelegateExecution execution) throws Exception {
    String reviewOutcome = (String) execution.getVariable("reviewOutcome");
    String reviewComment=  (String) execution.getVariable("reviewComment");


    if (reviewOutcome == null) {
      log.warn("reviewOutcome not set, defaulting to REJECTED");
      reviewOutcome = "REJECTED";
    }

    log.info("Applying manual review outcome: {}, comment: {}", reviewOutcome, reviewComment);
    execution.setVariable("fraudDecision", reviewOutcome);
  }
}
