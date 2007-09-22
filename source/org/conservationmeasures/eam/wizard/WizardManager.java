/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard;

import java.util.Hashtable;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.wizard.BudgetWizardAccountingAndFunding;
import org.conservationmeasures.eam.views.budget.wizard.BudgetWizardBudgetDetail;
import org.conservationmeasures.eam.views.budget.wizard.BudgetWizardDemo;
import org.conservationmeasures.eam.views.budget.wizard.FinancialOverviewStep;
import org.conservationmeasures.eam.views.diagram.wizard.DescribeTargetStatusStep;
import org.conservationmeasures.eam.views.diagram.wizard.DevelopDraftStrategiesStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramOverviewStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardCompleteResultsChainLinks;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardConstructChainsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardConstructInitialResultsChain;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardDefineTargetsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardGoodResultsChainCriteriaReview;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardIdentifyDirectThreatStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardIdentifyIndirectThreatStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardLinkDirectThreatsToTargetsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardProjectScopeStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardResultsChainStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardReviewAndModifyTargetsStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardReviewModelAndAdjustStep;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardVisionStep;
import org.conservationmeasures.eam.views.diagram.wizard.RankDraftStrategiesStep;
import org.conservationmeasures.eam.views.diagram.wizard.SelectChainStep;
import org.conservationmeasures.eam.views.images.wizard.LibraryOverviewStep;
import org.conservationmeasures.eam.views.map.wizard.MapOverviewStep;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringPlanOverviewStep;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringWizardDefineIndicatorsStep;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringWizardFocusStep;
import org.conservationmeasures.eam.views.noproject.NoProjectView;
import org.conservationmeasures.eam.views.noproject.wizard.NoProjectOverviewStep;
import org.conservationmeasures.eam.views.noproject.wizard.NoProjectWizardImportStep;
import org.conservationmeasures.eam.views.noproject.wizard.NoProjectWizardProjectCreateStep;
import org.conservationmeasures.eam.views.planning.wizard.PlanningOverviewStep;
import org.conservationmeasures.eam.views.planning.wizard.ReviewStratAndMonPlansStep;
import org.conservationmeasures.eam.views.schedule.wizard.ScheduleOverviewStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanDevelopGoalStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanDevelopObjectivesStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanOverviewStep;
import org.conservationmeasures.eam.views.summary.SummaryView;
import org.conservationmeasures.eam.views.summary.wizard.SummaryOverviewStep;
import org.conservationmeasures.eam.views.summary.wizard.SummaryWizardDefineProjecScope;
import org.conservationmeasures.eam.views.summary.wizard.SummaryWizardDefineProjectLeader;
import org.conservationmeasures.eam.views.summary.wizard.SummaryWizardDefineProjectVision;
import org.conservationmeasures.eam.views.summary.wizard.SummaryWizardDefineTeamMembers;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViability3Step;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViability4Step;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViability5Step;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViability6Step;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViability7Step;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViability8Step;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViabilityMethodChoiceStep;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViabilityOverviewAfterDetailedModeStep;
import org.conservationmeasures.eam.views.targetviability.wizard.TargetViabilityOverviewStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatMatrixOverviewStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardCheckBundleStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardCheckTotalsStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardChooseBundle;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardIrreversibilityStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardScopeStep;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardSeverityStep;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanAssignResourcesStep;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanCreateResourcesStep;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanDevelopActivitiesAndTasksStep;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanDevelopMethodsAndTasksStep;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanOverviewStep;


public class WizardManager
{
	
	public WizardManager(MainWindow mainWindowToUse) throws Exception
	{
		stepEntries = new Hashtable();
		mainWindow = mainWindowToUse;
		setCurrentStepName(getOverviewStepName(NoProjectView.getViewName()));
	}
	
	public void setUpSteps(WizardPanel panel) throws Exception
	{
		createThreatMatrixViewStepEntries(panel);
		createDigramViewStepEntries(panel);
		createMonitoringViewStepEntries(panel);
		createWorkPlanStepEntries(panel);
		createSummaryStepEntries(panel);
		createScheduleStepEntries(panel);
		createStrategicPlanStepEntries(panel);
		createBudgetStepEntries(panel);
		createNoProjectStepEntries(panel);
		createTargetViabilityStepEntries(panel);
		createMapViewStepEntries(panel);
		createImagesViewStepEntries(panel);
		createPlanningViewStepEntries(panel);
	}
	
	public String getCurrentStepName()
	{
		ProjectMetadata metadata = getProject().getMetadata();
		if(metadata == null)
			return nonProjectCurrentStepName;
		
		return  metadata.getCurrentWizardScreenName();
	}

	private void setCurrentStepName(String newStepName) throws CommandFailedException, Exception
	{
		ProjectMetadata metadata = getProject().getMetadata();
		if(metadata == null)
		{
			nonProjectCurrentStepName = newStepName;
			mainWindow.refreshWizard();
			return;
		}
		
		ORef projectMetadataRef = metadata.getRef();
		getProject().executeCommand(new CommandSetObjectData(projectMetadataRef, ProjectMetadata.TAG_CURRENT_WIZARD_SCREEN_NAME, newStepName));
	}

	public void setStep(Class stepClass) throws Exception
	{
		String name = stripJumpPrefix(stepClass);
		setStep(name);
	}
	
	public void setStep(String newStepName) throws Exception
	{
		SkeletonWizardStep newStep = findStep(newStepName);
		
		if (newStep == null)
		{
			EAM.logError("WizardManager couldnt find step " + newStepName);
			return;
		}
		
		setCurrentStepName(newStepName);

		String newViewName = newStep.getViewName();
		String currentViewName = getProject().getCurrentView();
		if(!newViewName.equals(currentViewName))
		{
			getProject().switchToView(newViewName);
		}

	}

	private SkeletonWizardStep getDefaultStep()
	{
		String stepName = getOverviewStepName(SummaryView.getViewName());
		return findStep(stepName);
	}

	public SkeletonWizardStep getCurrentStep()
	{
		SkeletonWizardStep step = findStep(getCurrentStepName());
		if(step == null)
			step = getDefaultStep();
		return step;
	}

	private Project getProject()
	{
		return mainWindow.getProject();
	}

	public void createNoProjectStepEntries(WizardPanel panel) throws Exception
	{	
		createStepEntry(new NoProjectOverviewStep(panel))
			.createControl("Import",NoProjectWizardImportStep.class)
			.createControl("NewProject",NoProjectWizardProjectCreateStep.class);
		
		createStepEntry(new NoProjectWizardImportStep(panel))
			.createBackControl(NoProjectOverviewStep.class);
		
		createStepEntry(new NoProjectWizardProjectCreateStep(panel))
			.createBackControl(NoProjectOverviewStep.class);
	}
	
	public void createBudgetStepEntries(WizardPanel panel)
	{
		createStepEntry(new FinancialOverviewStep(panel));
		createStepEntry(new BudgetWizardAccountingAndFunding(panel));
		createStepEntry(new BudgetWizardBudgetDetail(panel));
		createStepEntry(new BudgetWizardDemo(panel));
	}
	
	public void createStrategicPlanStepEntries(WizardPanel panel)
	{
		createStepEntry(new StrategicPlanOverviewStep(panel));
	}
	
	public void createScheduleStepEntries(WizardPanel panel)
	{
		createStepEntry(new ScheduleOverviewStep(panel));
	}
	
	public void createTargetViabilityStepEntries(WizardPanel panel)
	{
		createStepEntry(new TargetViabilityOverviewStep(panel)).
				createControl(CONTROL_NEXT, TargetViabilityMethodChoiceStep.class);
		createStepEntry(new TargetViabilityOverviewAfterDetailedModeStep(panel)).
				createControl(CONTROL_NEXT, TargetViabilityMethodChoiceStep.class);
	}
	
	public void createSummaryStepEntries(WizardPanel panel)
	{
		createStepEntry(new SummaryOverviewStep(panel));
		createStepEntry(new SummaryWizardDefineTeamMembers(panel));
		createStepEntry(new SummaryWizardDefineProjectLeader(panel));;
		createStepEntry(new SummaryWizardDefineProjecScope(panel));
		createStepEntry(new SummaryWizardDefineProjectVision(panel));
	}
	
	public void createWorkPlanStepEntries(WizardPanel panel)
	{
		createStepEntry(new WorkPlanOverviewStep(panel));
		createStepEntry(new WorkPlanDevelopActivitiesAndTasksStep(panel));
		createStepEntry(new WorkPlanDevelopMethodsAndTasksStep(panel));
		createStepEntry(new WorkPlanCreateResourcesStep(panel));
		createStepEntry(new WorkPlanAssignResourcesStep(panel));
	}
	
	public void createMonitoringViewStepEntries(WizardPanel panel)
	{		
		createStepEntry(new MonitoringPlanOverviewStep(panel));
	}
	
	public void createDigramViewStepEntries(WizardPanel panel)
	{
		createStepEntry(new DiagramOverviewStep(panel));
		createStepEntry(new DiagramWizardProjectScopeStep(panel));
		createStepEntry(new DiagramWizardVisionStep(panel));
		createStepEntry(new DiagramWizardDefineTargetsStep(panel));
		createStepEntry(new DiagramWizardReviewAndModifyTargetsStep(panel));
		//TODO rename TargetViability Classes to identif which step is which
		createStepEntry(new TargetViabilityMethodChoiceStep(panel)).
					createControl("DoneViabilityAnalysis", DiagramWizardIdentifyDirectThreatStep.class).
					createControl("DetailedViability", TargetViability3Step.class);
		createStepEntry(new DescribeTargetStatusStep(panel)).createControl(CONTROL_NEXT, TargetViabilityOverviewStep.class);
		createStepEntry(new TargetViability3Step(panel)).createControl(CONTROL_BACK, TargetViabilityMethodChoiceStep.class);
		createStepEntry(new TargetViability4Step(panel));
		createStepEntry(new TargetViability5Step(panel));
		createStepEntry(new TargetViability6Step(panel));
		createStepEntry(new TargetViability7Step(panel));
		createStepEntry(new TargetViability8Step(panel));
		
		createStepEntry(new DiagramWizardIdentifyDirectThreatStep(panel)).createControl(CONTROL_BACK, TargetViabilityMethodChoiceStep.class);
		createStepEntry(new DiagramWizardLinkDirectThreatsToTargetsStep(panel));
		createStepEntry(new DiagramWizardIdentifyIndirectThreatStep(panel));		
		createStepEntry(new DiagramWizardConstructChainsStep(panel));	
		createStepEntry(new DiagramWizardReviewModelAndAdjustStep(panel));		
		createStepEntry(new SelectChainStep(panel));		
		createStepEntry(new DevelopDraftStrategiesStep(panel));
		createStepEntry(new RankDraftStrategiesStep(panel));
		createStepEntry(new StrategicPlanDevelopGoalStep(panel));
		createStepEntry(new StrategicPlanDevelopObjectivesStep(panel));		
		createStepEntry(new MonitoringWizardFocusStep(panel));		
		createStepEntry(new MonitoringWizardDefineIndicatorsStep(panel));
	
		createStepEntry(new DiagramWizardResultsChainStep(panel));
		createStepEntry(new DiagramWizardConstructInitialResultsChain(panel));
		createStepEntry(new DiagramWizardCompleteResultsChainLinks(panel));
		createStepEntry(new DiagramWizardGoodResultsChainCriteriaReview(panel));
	}


	public void createThreatMatrixViewStepEntries(WizardPanel panel) throws Exception
	{
		//TODO: View:Diagram...should be Step:StepName or support both
		
		createStepEntry(new ThreatMatrixOverviewStep(panel))
			.createControl("View:Diagram", DiagramOverviewStep.class);

		createStepEntry(new ThreatRatingWizardChooseBundle(panel))
			.createControl("Done", ThreatRatingWizardCheckTotalsStep.class);
		
		createStepEntry(new ThreatRatingWizardScopeStep(panel));
		createStepEntry(new ThreatRatingWizardSeverityStep(panel));
		createStepEntry(new ThreatRatingWizardIrreversibilityStep(panel));

		createStepEntry(new ThreatRatingWizardCheckBundleStep(panel))
			.createNextControl(ThreatRatingWizardChooseBundle.class); 

		createStepEntry(new ThreatRatingWizardCheckTotalsStep(panel))
			.createBackControl(ThreatRatingWizardChooseBundle.class); 
	}

	public void createPlanningViewStepEntries(WizardPanel panel)
	{
		createStepEntry(new PlanningOverviewStep(panel));
		createStepEntry(new ReviewStratAndMonPlansStep(panel));
	}
	
	public void createMapViewStepEntries(WizardPanel panel)
	{		
		createStepEntry(new MapOverviewStep(panel));
	}
	
	public void createImagesViewStepEntries(WizardPanel panel)
	{
		createStepEntry(new LibraryOverviewStep(panel));
	}
	

	static Class[] getSequence()
	{
		Class[] entries = 
		{
				SummaryOverviewStep.class,
				
				// STEP 1A
				SummaryWizardDefineTeamMembers.class,
				SummaryWizardDefineProjectLeader.class,
				
				// STEP 1B
				SummaryWizardDefineProjecScope.class,
				SummaryWizardDefineProjectVision.class,
				DiagramOverviewStep.class,
				DiagramWizardProjectScopeStep.class,
				DiagramWizardVisionStep.class,
				DiagramWizardDefineTargetsStep.class,
				DiagramWizardReviewAndModifyTargetsStep.class,
				TargetViabilityMethodChoiceStep.class,
				DescribeTargetStatusStep.class,
				TargetViabilityOverviewStep.class,
				TargetViability3Step.class,
				TargetViability4Step.class,
				TargetViability5Step.class,
				TargetViability6Step.class,
				TargetViability7Step.class,
				TargetViability8Step.class,
				TargetViabilityOverviewAfterDetailedModeStep.class,			

				// STEP 1C
				DiagramWizardIdentifyDirectThreatStep.class,
				DiagramWizardLinkDirectThreatsToTargetsStep.class,
				ThreatMatrixOverviewStep.class,
				ThreatRatingWizardChooseBundle.class,
				ThreatRatingWizardScopeStep.class,
				ThreatRatingWizardSeverityStep.class,
				ThreatRatingWizardIrreversibilityStep.class,
				ThreatRatingWizardCheckBundleStep.class,
				ThreatRatingWizardCheckTotalsStep.class,
				
				// STEP 1D
				DiagramWizardIdentifyIndirectThreatStep.class,		
				DiagramWizardConstructChainsStep.class,	
				DiagramWizardReviewModelAndAdjustStep.class,		
				
				// STEP 2A
				StrategicPlanOverviewStep.class,
				StrategicPlanDevelopGoalStep.class,
				StrategicPlanDevelopObjectivesStep.class, 	

				// STEP 2B
				SelectChainStep.class,		
				DevelopDraftStrategiesStep.class,
				RankDraftStrategiesStep.class,
				DiagramWizardResultsChainStep.class,
				DiagramWizardConstructInitialResultsChain.class,
				DiagramWizardCompleteResultsChainLinks.class,
				DiagramWizardGoodResultsChainCriteriaReview.class,

				// STEP 3A
				MonitoringPlanOverviewStep.class,
				MonitoringWizardFocusStep.class,
				
				// STEP 3B
				MonitoringWizardDefineIndicatorsStep.class,
				PlanningOverviewStep.class,
				ReviewStratAndMonPlansStep.class,
				
				// STEP 4A
				WorkPlanOverviewStep.class,
				WorkPlanDevelopActivitiesAndTasksStep.class,
				WorkPlanDevelopMethodsAndTasksStep.class,
				WorkPlanCreateResourcesStep.class,
				WorkPlanAssignResourcesStep.class,
				FinancialOverviewStep.class, 
				BudgetWizardAccountingAndFunding.class,
				BudgetWizardBudgetDetail.class,
				BudgetWizardDemo.class, 
				
				// NOT STEPS
				ScheduleOverviewStep.class,
				MapOverviewStep.class,
				LibraryOverviewStep.class,
		};
		
		return entries;
		
	}
	
	private SkeletonWizardStep createStepEntry(SkeletonWizardStep step)
	{
		stepEntries.put(step.getClass().getSimpleName(),step);
		return step;
	}

	
	public String stripJumpPrefix(Class stepMarker)
	{
		String name = stepMarker.getSimpleName();
		final String prefix = "ActionJump";
		if (name.startsWith(prefix))
			name = name.substring(prefix.length());
		return name;
	}
	
	
	public boolean isValidStep(Class stepMarker)
	{
		String name = stripJumpPrefix(stepMarker);
		return (stepEntries.get(name)!=null);
	}
	
	public SkeletonWizardStep findStep(String stepName)
	{
		SkeletonWizardStep step =(SkeletonWizardStep)stepEntries.get(stepName);
		if (step==null)
			EAM.logVerbose("ENTRY NOT FOUND FOR STEP NAME=:" + stepName);
		return step;
	}
	
	public Class findControlTargetStep(String controlName, SkeletonWizardStep step)
	{
		Class targetStep = step.getControl(controlName);
		
		if (targetStep==null)
			return doDeferedSequenceLookup(controlName,step);

		return targetStep;
	}
	
	Class doDeferedSequenceLookup(String controlName, SkeletonWizardStep step)
	{
		Class name = getDestinationStep(controlName, step);
		return name;
	}

	public Class getDestinationStep(String controlName, SkeletonWizardStep step)
	{
		Class[] sequences = WizardManager.getSequence();

		int position = findPositionInSequence(sequences, step);
		if (position<0)
			return null;
		
		if (controlName.equals(CONTROL_NEXT))
			++position;
		
		if (controlName.equals(CONTROL_BACK))
			--position;
		
		if (position<0 || position>=sequences.length)
			return null;
		
		return sequences[position];
	}


	private int findPositionInSequence(Class[] sequences, SkeletonWizardStep step)
	{
		for (int i=0; i<sequences.length; ++i)
			if (sequences[i].getSimpleName().equals(getStepName(step))) 
				return i;
		return -1;
	}
	
	public static String getStepName(SkeletonWizardStep step)
	{
		return step.getClass().getSimpleName();
	}
	
	public void setOverViewStep(String viewName) throws Exception
	{
		setStep(getOverviewStepName(viewName));
	}

	public String getOverviewStepName(String viewName)
	{
		return removeSpaces(viewName) + "OverviewStep";
	}
	
	private String removeSpaces(String name)
	{
		return name.replaceAll(" ", "");
	}

	public static String CONTROL_NEXT = "Next";
	public static String CONTROL_BACK = "Back";

	private MainWindow mainWindow;
	private Hashtable stepEntries;
	
	private String nonProjectCurrentStepName;
}

