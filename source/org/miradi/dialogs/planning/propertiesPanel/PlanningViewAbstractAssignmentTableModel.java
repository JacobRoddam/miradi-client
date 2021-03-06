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
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.CurrencyFormat;
import org.miradi.project.Project;

abstract public class PlanningViewAbstractAssignmentTableModel extends EditableObjectTableModel
{
	public PlanningViewAbstractAssignmentTableModel(Project projectToUse)
	{
		super(projectToUse);
		
		baseObjectRefs = new ORefList();
		currencyFormatter = getProject().getCurrencyFormatterWithCommas();
	}
	
	public int getRowCount()
	{
		return baseObjectRefs.size();
	}
	
	@Override
	public void setObjectRefs(ORefList hierarchyToSelectedRef)
	{
		if(hierarchyToSelectedRef.size() == 0)
			return;
		
		ORef selectedRef = hierarchyToSelectedRef.getFirstElement();
		
		baseObject = BaseObject.find(getProject(), selectedRef);
		baseObjectRefs = getRefsForBaseObject(baseObject);
	}
	
	@Override
	protected ORefList getRowObjectRefs()
	{
		return baseObjectRefs;
	}
			
	public void setBaseObject(BaseObject baseObjectToUse)
	{
		if (isAlreadyCurrentBaseObject(baseObjectToUse))
			return;
			
		baseObject = baseObjectToUse;
		updateRefList();	
	}
	
	public void dataWasChanged() throws Exception
	{
		if (isAlreadyCurrentRefList())
			return;
		
		updateRefList();
	}

	private boolean isAlreadyCurrentBaseObject(BaseObject baseObjectToUse)
	{
		 if(baseObject == null || baseObjectToUse == null)
			 return false;
		 
		 return baseObject.getId().equals(baseObjectToUse.getId());
	}
	
	private boolean isAlreadyCurrentRefList()
	{
		return baseObjectRefs.equals(getRefsForBaseObject(baseObject));
	}
	
	private void updateRefList()
	{
		baseObjectRefs = getRefsForBaseObject(baseObject);
		fireTableDataChanged();
	}
		
	private ORefList getRefsForBaseObject(BaseObject baseObjectToUse)
	{
		if (baseObjectToUse == null)
			return new ORefList();
		
		try
		{
			return baseObjectToUse.getSafeRefListData(getListTag());	
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new ORefList();
		}
	}
	
	public ORef getRefForRow(int row)
	{
		return baseObjectRefs.get(row);
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return BaseObject.find(getProject(), getRefForRow(row));
	}
	
	abstract protected String getListTag();
	
	abstract protected int getListType();
	
	protected ORefList baseObjectRefs;
	protected BaseObject baseObject;

	protected CurrencyFormat currencyFormatter;
}
