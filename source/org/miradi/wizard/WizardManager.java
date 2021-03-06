/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.wizard;

import org.miradi.actions.openstandards.*;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.main.Miradi;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.views.noproject.NoProjectView;
import org.miradi.views.summary.SummaryView;
import org.miradi.wizard.diagram.*;
import org.miradi.wizard.library.LibraryOverviewStep;
import org.miradi.wizard.map.MapOverviewStep;
import org.miradi.wizard.noproject.NoProjectOverviewStep;
import org.miradi.wizard.noproject.WelcomeCreateStep;
import org.miradi.wizard.noproject.WelcomeImportStep;
import org.miradi.wizard.planning.*;
import org.miradi.wizard.reports.ReportsOverviewStep;
import org.miradi.wizard.schedule.ScheduleOverviewStep;
import org.miradi.wizard.summary.*;
import org.miradi.wizard.targetviability.*;
import org.miradi.wizard.threatmatrix.*;
import org.miradi.wizard.workplan.WorkPlanOverviewStep;

import java.util.Hashtable;


public class WizardManager
{
	
	public WizardManager(MainWindow mainWindowToUse) throws Exception
	{
		stepEntries = new Hashtable<String, SkeletonWizardStep>();
		mainWindow = mainWindowToUse;
		setCurrentStepName(getOverviewStepName(NoProjectView.getViewName()));
	}
	
	public void setUpSteps(WizardPanel panel) throws Exception
	{
		createNoProjectStepEntries(panel);
		createSummaryStepEntries(panel);
		createDiagramViewStepEntries(panel);
		createTargetViabilityStepEntries(panel);
		createThreatMatrixViewStepEntries(panel);
		createPlanningViewStepEntries(panel);
		createWorkPlanViewStepEntries(panel);
		createReportViewStepEntries(panel);
		createScheduleStepEntries(panel);
		createMapViewStepEntries(panel);
		createImagesViewStepEntries(panel);
		
		createDashboardStepEntries(panel);
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
		
		ViewData viewData = getProject().getCurrentViewData();
		ORef projectMetadataRef = metadata.getRef();
		getProject().executeCommand(new CommandSetObjectData(projectMetadataRef, ProjectMetadata.TAG_CURRENT_WIZARD_SCREEN_NAME, newStepName));
		getProject().executeCommand(new CommandSetObjectData(viewData, ViewData.TAG_CURRENT_WIZARD_STEP, newStepName));
		
		if(Miradi.isDeveloperMode())
			EAM.logDebug(newStepName);
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
		createStepEntry(new NoProjectOverviewStep(panel));
		
		createStepEntry(new WelcomeImportStep(panel))
			.createBackControl(NoProjectOverviewStep.class);
		
		createStepEntry(new WelcomeCreateStep(panel))
			.createBackControl(NoProjectOverviewStep.class);
	}
	
	public void createScheduleStepEntries(WizardPanel panel)
	{
		createStepEntry(new ScheduleOverviewStep(panel));
	}
	
	public void createTargetViabilityStepEntries(WizardPanel panel)
	{
		createStepEntry(new TargetViabilityOverviewStep(panel));
		
		createStepEntry(new DescribeTargetStatusStep(panel)).
			createControl(CONTROL_NEXT, TargetStressesStep.class);
		createStepEntry(new TargetViabilityDetermineKeyAttributesStep(panel)).
			createControl(CONTROL_BACK, TargetViabilityMethodChoiceStep.class);
		createStepEntry(new TargetViabilityIdentifyIndicatorsStep(panel));
		createStepEntry(new TargetViabilityDevelopDraftIndicatorsStep(panel));
		createStepEntry(new TargetViabilityRefineIndicatorRatingsStep(panel));
		createStepEntry(new TargetViabilityRecordInitialMeasurementStep(panel));
		createStepEntry(new TargetViabilityDetermineDesiredStatusStep(panel));
	}
	
	public void createSummaryStepEntries(WizardPanel panel)
	{
		createStepEntry(new SummaryOverviewStep(panel));
		createStepEntry(new SummaryWizardDefineTeamMembers(panel));
		createStepEntry(new SummaryWizardRolesAndResponsibilities(panel));
		createStepEntry(new SummaryWizardDefineProjecScope(panel));
		createStepEntry(new SummaryWizardDefineProjectVision(panel));
	}
	
	public void createDiagramViewStepEntries(WizardPanel panel)
	{
		createStepEntry(new DiagramOverviewStep(panel));
		createStepEntry(new DiagramWizardProjectScopeStep(panel));
		createStepEntry(new DiagramWizardDefineTargetsStep(panel));
		createStepEntry(new DiagramWizardReviewAndModifyTargetsStep(panel));
		createStepEntry(new DiagramWizardHumanWelfareTargetsStep(panel));
		//TODO rename TargetViability Classes to identify which step is which
		createStepEntry(new TargetViabilityMethodChoiceStep(panel)).
			createControl("DoneViabilityAnalysis", TargetStressesStep.class).
			createControl(CONTROL_NEXT, TargetStressesStep.class).
			createControl("DetailedViability", TargetViabilityDetermineKeyAttributesStep.class).
			createControl("SimpleMode", DescribeTargetStatusStep.class);
	
		createStepEntry(new TargetStressesStep(panel))
			.createControl(CONTROL_BACK, TargetViabilityMethodChoiceStep.class);
		
		createStepEntry(new DiagramWizardIdentifyDirectThreatStep(panel));
		createStepEntry(new DiagramWizardLinkDirectThreatsToTargetsStep(panel));
		createStepEntry(new DiagramWizardIdentifyIndirectThreatStep(panel));		
		createStepEntry(new DiagramWizardCreateInitialModelStep(panel));	
		createStepEntry(new DiagramWizardReviewModelAndAdjustStep(panel));		
		createStepEntry(new SelectChainStep(panel));		
		createStepEntry(new DevelopDraftStrategiesStep(panel));
		createStepEntry(new RankDraftStrategiesStep(panel));
		createStepEntry(new StrategicPlanDevelopGoalStep(panel));
		createStepEntry(new StrategicPlanDevelopObjectivesStep(panel));		
		createStepEntry(new DiagramWizardDefineAudienceStep(panel));		
		createStepEntry(new MonitoringWizardDefineIndicatorsStep(panel));
		createStepEntry(new PlanningWizardFinalizeMonitoringPlanStep(panel));
	
		createStepEntry(new DiagramWizardResultsChainSelectStrategyStep(panel));
		createStepEntry(new DiagramWizardConstructInitialResultsChain(panel));
		createStepEntry(new DiagramWizardCompleteResultsChainLinks(panel));
		createStepEntry(new DiagramWizardGoodResultsChainCriteriaReview(panel));
	}


	public void createThreatMatrixViewStepEntries(WizardPanel panel) throws Exception
	{
		//TODO: View:Diagram...should be Step:StepName or support both
		
		createStepEntry(new ThreatMatrixOverviewStep(panel))
			.createControl("View:Diagram", DiagramOverviewStep.class)
			.createControl(ThreatMatrixOverviewStep.THREAT_OVERVIEW_STRESS_MODE, ThreatStressOverviewStep.class);
		
		createStepEntry(new ThreatSimpleOverviewStep(panel));
		createStepEntry(new ThreatRatingWizardChooseBundle(panel))
			.createControl("Done", ThreatRatingWizardCheckTotalsStep.class);
		
		createStepEntry(new ThreatRatingWizardScopeStep(panel));
		createStepEntry(new ThreatRatingWizardSeverityStep(panel));
		createStepEntry(new ThreatRatingWizardIrreversibilityStep(panel));

		createStepEntry(new ThreatRatingWizardCheckBundleStep(panel))
			.createNextControl(ThreatRatingWizardChooseBundle.class); 

		createStepEntry(new ThreatRatingWizardCheckTotalsStep(panel))
			.createBackControl(ThreatStressOverviewStep.class)
			.createControl(ThreatRatingWizardCheckTotalsStep.END_OF_THREAT_SIMPLE_BRANCH, ThreatRatingWizardChooseBundle.class); 

		createStepEntry(new ThreatStressOverviewStep(panel))
			.createBackControl(ThreatMatrixOverviewStep.class)
			.createControl("Done", ThreatRatingWizardCheckTotalsStep.class);
		createStepEntry(new ThreatStressRateScopeAndSeverityStep(panel));
		createStepEntry(new ThreatStressRateContributionAndIrreversibilityStep(panel));
		createStepEntry(new ThreatStressCheckThreatRatingStep(panel))
			.createNextControl(ThreatStressOverviewStep.class);

	}

	public void createPlanningViewStepEntries(WizardPanel panel)
	{
		createStepEntry(new PlanningOverviewStep(panel));
		createStepEntry(new PlanningWizardFinalizeStrategicPlanStep(panel));
		
		createStepEntry(new DevelopOperationalPlan(panel));
	}

	public void createWorkPlanViewStepEntries(WizardPanel panel)
	{
		createStepEntry(new WorkPlanOverviewStep(panel));
		createStepEntry(new WorkPlanDevelopActivitiesAndTasksStep(panel));
		createStepEntry(new WorkPlanDevelopMethodsAndTasksStep(panel));
		createStepEntry(new WorkPlanCreateResourcesStep(panel));
		createStepEntry(new BudgetWizardAccountingAndFunding(panel));
		createStepEntry(new BudgetWizardBudgetDetail(panel));
		createStepEntry(new BudgetWizardReconcileActualExpendituresStep(panel));
		createStepEntry(new ImplementPlans(panel));
	}
	
	public void createReportViewStepEntries(WizardPanel panel)
	{
		createStepEntry(new ReportsOverviewStep(panel));
	}
	
	public void createMapViewStepEntries(WizardPanel panel)
	{		
		createStepEntry(new MapOverviewStep(panel));
	}
	
	public void createImagesViewStepEntries(WizardPanel panel)
	{
		createStepEntry(new LibraryOverviewStep(panel));
	}
	
	public void createDashboardStepEntries(WizardPanel panel) throws Exception
	{
		/* 1  */ createStepEntry(new SummaryWizardDefineTeamMembers(panel), ActionOpenStandardsConceptualizeParentMenu.class);
		/* 1a */ createStepEntry(new SummaryWizardDefineTeamMembers(panel), ActionOpenStandardsConceptualizeProcessStep1a.class);
		/* 1b */ createStepEntry(new SummaryWizardDefineProjecScope(panel), ActionOpenStandardsConceptualizeProcessStep1b.class);
		/* 1c */ createStepEntry(new DiagramWizardIdentifyDirectThreatStep(panel), ActionOpenStandardsConceptualizeProcessStep1c.class);
		/* 1d */ createStepEntry(new DiagramWizardIdentifyIndirectThreatStep(panel), ActionOpenStandardsConceptualizeProcessStep1d.class);

		/* 2  */ createStepEntry(new StrategicPlanDevelopGoalStep(panel), ActionOpenStandardsPlanActionsAndMonitoringParentMenu.class);
		/* 2a */ createStepEntry(new StrategicPlanDevelopGoalStep(panel), ActionOpenStandardsPlanActionsAndMonitoringProcessStep2a.class);
		/* 2b */ createStepEntry(new DiagramWizardDefineAudienceStep(panel), ActionOpenStandardsPlanActionsAndMonitoringProcessStep2b.class);
		/* 2c */ createStepEntry(new DevelopOperationalPlan(panel), ActionOpenStandardsPlanActionsAndMonitoringProcessStep2c.class);

		/* 3  */ createStepEntry(new WorkPlanOverviewStep(panel), ActionOpenStandardsImplementActionsAndMonitoringParentMenu.class);
		/* 3a */ createStepEntry(new WorkPlanOverviewStep(panel), ActionOpenStandardsImplementActionsAndMonitoringProcessStep3a.class);
		/* 3b */ createStepEntry(new BudgetWizardAccountingAndFunding(panel), ActionOpenStandardsImplementActionsAndMonitoringProcessStep3b.class);
		/* 3c */ createStepEntry(new ImplementPlans(panel), ActionOpenStandardsImplementActionsAndMonitoringProcessStep3c.class);
		
		/* 4  */ createStepEntry(new ImplementPlans(panel), ActionOpenStandardsAnalyzeUseAndAdaptParentMenu.class);
		/* 4a */ createStepEntry(new ImplementPlans(panel), ActionOpenStandardsAnalyzeUseAndAdaptProcessStep4a.class);
		/* 4b */ createStepEntry(new ImplementPlans(panel), ActionOpenStandardsAnalyzeUseAndAdaptProcessStep4b.class);
		/* 4c */ createStepEntry(new ImplementPlans(panel), ActionOpenStandardsAnalyzeUseAndAdaptProcessStep4c.class);
		
		/* 5 */ createStepEntry(new ReportsOverviewStep(panel), ActionOpenStandardsCaptureAndShareLearningParentMenu.class);
		/* 5a */ createStepEntry(new ReportsOverviewStep(panel), ActionOpenStandardsCaptureAndShareLearningProcessStep5a.class);
		/* 5b */ createStepEntry(new ReportsOverviewStep(panel), ActionOpenStandardsCaptureAndShareLearningProcessStep5b.class);
		/* 5c */ createStepEntry(new ReportsOverviewStep(panel), ActionOpenStandardsCaptureAndShareLearningProcessStep5c.class);
	}

	static Class[] getSequence()
	{
		Class[] entries = 
		{
				SummaryOverviewStep.class,
				
				// STEP 1A
				SummaryWizardDefineTeamMembers.class,
				SummaryWizardRolesAndResponsibilities.class,
				
				// STEP 1B
				SummaryWizardDefineProjecScope.class,

				// (later this will go here)
				//MapOverviewStep.class,

				SummaryWizardDefineProjectVision.class,
				DiagramOverviewStep.class,
				DiagramWizardProjectScopeStep.class,

				DiagramWizardDefineTargetsStep.class,
				DiagramWizardReviewAndModifyTargetsStep.class,
				DiagramWizardHumanWelfareTargetsStep.class,
				
				TargetViabilityOverviewStep.class,
				TargetViabilityMethodChoiceStep.class,
				DescribeTargetStatusStep.class,
				TargetViabilityDetermineKeyAttributesStep.class,
				TargetViabilityIdentifyIndicatorsStep.class,
				TargetViabilityDevelopDraftIndicatorsStep.class,
				TargetViabilityRefineIndicatorRatingsStep.class,
				TargetViabilityRecordInitialMeasurementStep.class,
				TargetViabilityDetermineDesiredStatusStep.class,
				
				TargetStressesStep.class,

				// STEP 1C
				DiagramWizardIdentifyDirectThreatStep.class,
				DiagramWizardLinkDirectThreatsToTargetsStep.class,

				ThreatMatrixOverviewStep.class,
				
				// NOTE: The following is a loop
				ThreatSimpleOverviewStep.class,
				ThreatRatingWizardChooseBundle.class,
				ThreatRatingWizardScopeStep.class,
				ThreatRatingWizardSeverityStep.class,
				ThreatRatingWizardIrreversibilityStep.class,
				ThreatRatingWizardCheckBundleStep.class,
				
				// NOTE: The following is a loop
				ThreatStressOverviewStep.class,
				ThreatStressRateScopeAndSeverityStep.class,
				ThreatStressRateContributionAndIrreversibilityStep.class,
				ThreatStressCheckThreatRatingStep.class,
				
				ThreatRatingWizardCheckTotalsStep.class,

				// STEP 1D
				DiagramWizardIdentifyIndirectThreatStep.class,	
				// need AssessStakeholders here
				DiagramWizardCreateInitialModelStep.class,	
				DiagramWizardReviewModelAndAdjustStep.class,		
				
				// STEP 2A
//				StrategicPlanOverviewStep.class,
				StrategicPlanDevelopGoalStep.class,
				
				SelectChainStep.class,		
				DevelopDraftStrategiesStep.class,
				
				RankDraftStrategiesStep.class,

				DiagramWizardResultsChainSelectStrategyStep.class,
				DiagramWizardConstructInitialResultsChain.class,
				DiagramWizardCompleteResultsChainLinks.class,
				DiagramWizardGoodResultsChainCriteriaReview.class,

				StrategicPlanDevelopObjectivesStep.class, 	

				PlanningOverviewStep.class,
				PlanningWizardFinalizeStrategicPlanStep.class,

				// STEP 2B
//				MonitoringPlanOverviewStep.class,
				DiagramWizardDefineAudienceStep.class,
				MonitoringWizardDefineIndicatorsStep.class,
				PlanningWizardFinalizeMonitoringPlanStep.class,
				
				// STEP 2C
				DevelopOperationalPlan.class,

				// STEP 3A
				WorkPlanOverviewStep.class,
				WorkPlanDevelopActivitiesAndTasksStep.class,
				
				WorkPlanDevelopMethodsAndTasksStep.class,
				WorkPlanCreateResourcesStep.class,

				// (later this will go here)
				//ScheduleOverviewStep.class,
				
				// STEP 3B
				BudgetWizardAccountingAndFunding.class,
				BudgetWizardBudgetDetail.class,
				BudgetWizardReconcileActualExpendituresStep.class,
				
				// STEP 3C
				ImplementPlans.class,
				
				// STEP 4A
//				FinancialOverviewStep.class, 
				
				// NOT STEPS
				ReportsOverviewStep.class,
				MapOverviewStep.class,
				ScheduleOverviewStep.class,
				LibraryOverviewStep.class,
		};
		
		return entries;
		
	}
	
	private SkeletonWizardStep createStepEntry(SkeletonWizardStep step)
	{
		createStepEntry(step, step.getClass());
		return step;
	}

	private void createStepEntry(SkeletonWizardStep step, Class classToUse)
	{
		stepEntries.put(classToUse.getSimpleName(), step);
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
		SkeletonWizardStep step = stepEntries.get(stepName);
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
	private Hashtable<String, SkeletonWizardStep> stepEntries;
	
	private String nonProjectCurrentStepName;
}

