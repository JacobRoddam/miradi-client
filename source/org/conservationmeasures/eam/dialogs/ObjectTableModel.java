/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;

abstract public class ObjectTableModel extends AbstractTableModel
{
	public ObjectTableModel(Project projectToUse, int listedItemType, String[] tableColumnTags)
	{
		columnTags = tableColumnTags;
		project = projectToUse;
		rowObjectType = listedItemType;
	}
	
	abstract public IdList getLatestIdListFromProject();
	
	public int getRowCount()
	{
		return getIdList().size();
	}
	
	void setNewRowOrder(Integer[] existingRowIndexesInNewOrder)
	{
		IdList newList = new IdList();
		for(int i = 0; i < existingRowIndexesInNewOrder.length; ++i)
		{
			int nextExistingRowIndex = existingRowIndexesInNewOrder[i].intValue();
			newList.add(rowObjectIds.get(nextExistingRowIndex));
		}
		rowObjectIds = newList;
	}

	public EAMObject getObjectFromRow(int row) throws RuntimeException
	{
		try
		{
			BaseId rowObjectId = getIdList().get(row);
			EAMObject rowObject = project.findObject(rowObjectType, rowObjectId);
			if(rowObject == null)
				throw new RuntimeException("ObjectTableModel.getObjectFromRow: Missing object: " + new ORef(rowObjectType, rowObjectId));
			return rowObject;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new RuntimeException("TeamModel.getObjectFromRow error");
		}
	}
	
	public int findRowObject(BaseId id)
	{
		for(int row = 0; row < getRowCount(); ++row)
		{
			if(getObjectFromRow(row).getId().equals(id))
				return row;
		}
		
		return -1;
	}

	public int getRowObjectType()
	{
		return rowObjectType;
	}
	
	public Object getValueAt(int row, int column)
	{
		try
		{
			EAMObject rowObject;
			rowObject = getObjectFromRow(row);
			return rowObject.getData(getColumnTag(column));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "(Error)";
		}
	}


	public void rowsWereAddedOrRemoved()
	{
		IdList availableIds = getLatestIdListFromProject();
		IdList newList = new IdList();
		for(int i = 0; i < rowObjectIds.size(); ++i)
		{
			BaseId thisId = rowObjectIds.get(i);
			if(availableIds.contains(thisId))
			{
				newList.add(thisId);
				availableIds.removeId(thisId);
			}
		}
		for(int i = 0; i < availableIds.size(); ++i)
		{
			newList.add(availableIds.get(i));
		}
		rowObjectIds = newList;
		fireTableDataChanged();
	}


	public IdList getIdList()
	{
		return rowObjectIds;
	}

	public String getColumnTag(int column)
	{
		return columnTags[column];
	}
	
	public int getColumnCount()
	{
		return columnTags.length;
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(rowObjectType, getColumnTag(column));
	}


	Project project;
	int rowObjectType;
	IdList rowObjectIds;
	String[] columnTags;
}
