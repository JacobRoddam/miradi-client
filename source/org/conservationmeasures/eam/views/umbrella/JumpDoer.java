/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.actions.jump.ActionJumpAssignResources;
import org.conservationmeasures.eam.actions.jump.ActionJumpBudgetFutureDemo;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreateModel;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineScope;
import org.conservationmeasures.eam.actions.jump.ActionJumpDescribeTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDesignateLeader;
import org.conservationmeasures.eam.actions.jump.ActionJumpDetermineNeeds;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopActivitiesAndTasks;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopBudgets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopMonitoringMethodsAndTasks;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopObjectives;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopSchedule;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopTargetGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramOverview;
import org.conservationmeasures.eam.actions.jump.ActionJumpEditIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpEstablishVision;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyContributingFactors;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpLinkDirectThreatsToTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringOverview;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDraftStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpReviewModelAndAdjust;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectMethod;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectTeam;
import org.conservationmeasures.eam.actions.jump.ActionJumpStratPlanWelcome;
import org.conservationmeasures.eam.actions.jump.ActionJumpThreatRatingWizardCheckTotals;
import org.conservationmeasures.eam.actions.jump.ActionJumpViewAllGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpViewAllObjectives;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanAssignResourcesStep;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSwitchView;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.utils.JumpLocation;
import org.conservationmeasures.eam.views.MainWindowDoer;
import org.conservationmeasures.eam.views.budget.BudgetView;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.diagram.wizard.ActionJumpEditAllStrategies;
import org.conservationmeasures.eam.views.monitoring.MonitoringView;
import org.conservationmeasures.eam.views.schedule.ScheduleView;
import org.conservationmeasures.eam.views.strategicplan.StrategicPlanView;
import org.conservationmeasures.eam.views.summary.SummaryView;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.conservationmeasures.eam.views.workplan.WorkPlanView;

public class JumpDoer extends MainWindowDoer
{
	public JumpDoer(Class actionClassToUse)
	{
		actionClass = actionClassToUse;
	}
	
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		JumpLocation jumpTo = createJumpLocation(actionClass);
		if(jumpTo == null)
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			JumpLocation jumpTo = createJumpLocation(actionClass);
			String view = jumpTo.getView();
			if(!getProject().getCurrentView().equals(view))
			{
				getMainWindow().setSplitterLocationToMiddle(view);
				getProject().executeCommand(new CommandSwitchView(jumpTo.getView()));
			}
			
			// FIXME: This really should be a Command so it is undoable,
			// but that would require us to be able to obtain the current 
			// step marker no matter where we are, which isn't possible yet
			getMainWindow().jump(jumpTo.getStepMarker());
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	public JumpLocation createJumpLocation(Class jumpActionClass)
	{
		String jumpToView = getViewForAction(jumpActionClass);
		if(jumpToView == null)
			return null;
		
		return new JumpLocation(jumpToView, jumpActionClass);
	}
	
	String getViewForAction(Class jumpActionClass)
	{
		if(jumpActionClass.equals(ActionJumpSelectTeam.class))
			return SummaryView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpDesignateLeader.class))
			return SummaryView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpDefineScope.class))
			return SummaryView.getViewName();

		if(jumpActionClass.equals(ActionJumpEstablishVision.class))
			return SummaryView.getViewName();
		
		
		if(jumpActionClass.equals(ActionJumpIdentifyTargets.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpDescribeTargets.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpIdentifyDirectThreats.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpIdentifyContributingFactors.class))
			return DiagramView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpDevelopTargetGoals.class))
			return DiagramView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpDevelopObjectives.class))
			return DiagramView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpDetermineNeeds.class))
			return DiagramView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpIdentifyStrategies.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpRankDraftStrategies.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpEditAllStrategies.class))
			return DiagramView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpReviewModelAndAdjust.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpLinkDirectThreatsToTargets.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpCreateModel.class))
			return DiagramView.getViewName();
		
		if(jumpActionClass.equals(ActionJumpDefineIndicators.class))
			return DiagramView.getViewName();
		
		

		if(jumpActionClass.equals(ActionJumpRankDirectThreats.class))
			return ThreatMatrixView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpThreatRatingWizardCheckTotals.class))
			return ThreatMatrixView.getViewName();
		

		if (jumpActionClass.equals(ActionJumpViewAllGoals.class))
			return StrategicPlanView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpViewAllObjectives.class))
			return StrategicPlanView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpStratPlanWelcome.class))
			return StrategicPlanView.getViewName();
		

		if (jumpActionClass.equals(ActionJumpEditIndicators.class))
			return MonitoringView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpMonitoringOverview.class))
			return MonitoringView.getViewName();
		
		
		if (jumpActionClass.equals(ActionJumpSelectMethod.class))
			return WorkPlanView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpWorkPlanAssignResourcesStep.class))
			return WorkPlanView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpDevelopActivitiesAndTasks.class))
			return WorkPlanView.getViewName();

		if (jumpActionClass.equals(ActionJumpDevelopMonitoringMethodsAndTasks.class))
			return WorkPlanView.getViewName();

		if (jumpActionClass.equals(ActionJumpAssignResources.class))
			return WorkPlanView.getViewName();

		
		if (jumpActionClass.equals(ActionJumpDevelopBudgets.class))
			return BudgetView.getViewName();
		if(jumpActionClass.equals(ActionJumpBudgetFutureDemo.class))
			return BudgetView.getViewName();

		
		if (jumpActionClass.equals(ActionJumpDevelopSchedule.class))
			return ScheduleView.getViewName();
		
		if (jumpActionClass.equals(ActionJumpDiagramOverview.class))
			return DiagramView.getViewName();
		
		return null;
	}

	Class actionClass;
}
