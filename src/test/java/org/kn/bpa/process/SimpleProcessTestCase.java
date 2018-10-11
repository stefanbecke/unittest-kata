package org.kn.bpa.process;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author stefan.becke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class SimpleProcessTestCase {

    public static final String BPMN_FILE = "CallingParentProcess.bpmn";
    public static final String PROCESS_KEY = "CallingParentProcess";
    public static final String VARIABLE_TRANSIENT_NAME = "transient";
    public static final String VARIABLE_PERSISTENCE_NAME = "persistence";

    @Autowired
    @Rule
    public ProcessEngineRule rule;

    @Autowired
    RuntimeService runtimeService;

    @Before
    public void init() {
        LogFactory.useSlf4jLogging();
    }

    @Test
    public void shouldNotFindTransientVariable() {
        deploy();

        // when we start the process
        final ProcessInstance procInst = runtimeService.startProcessInstanceByKey(PROCESS_KEY);

        String processVariablePeristent = (String) runtimeService.getVariable(procInst.getId(), VARIABLE_PERSISTENCE_NAME);
        assertThat(processVariablePeristent).isEqualTo("foo");

        String processVariablTransient = (String) runtimeService.getVariable(procInst.getId(), VARIABLE_TRANSIENT_NAME);
        assertThat(processVariablTransient).isNull();
    }

    private void deploy() {
        Deployment deploy = rule.getRepositoryService().createDeployment()
                .addModelInstance(BPMN_FILE, createProcess())
                .deploy();

        rule.manageDeployment(deploy);
    }

    private BpmnModelInstance createProcess() {

        return Bpmn.createExecutableProcess(PROCESS_KEY)
                .startEvent()
                .serviceTask("ServiceTask")
                .camundaDelegateExpression("${demoDelegate}")
                .userTask("userTask")
                .endEvent()
                .done();
    }

    public static class DemoDelegate implements JavaDelegate {

        @Override
        public void execute(DelegateExecution execution) {
            execution.setVariable(VARIABLE_PERSISTENCE_NAME, Variables.stringValue("foo", false));
            execution.setVariable(VARIABLE_TRANSIENT_NAME, Variables.stringValue("bar", true));
        }
    }
}
