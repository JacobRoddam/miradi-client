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
package org.miradi.views.summary;

import org.miradi.actions.views.ActionViewSummary;
import org.miradi.dialogfields.ObjectDataField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.forms.summary.ProjectTabForm;
import org.miradi.icons.MiradiApplicationIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ProjectMetadata;
import org.miradi.questions.MajorLanguagesQuestion;
import org.miradi.rtf.RtfFormExporter;
import org.miradi.rtf.RtfWriter;
import org.miradi.schemas.MiradiShareProjectDataSchema;
import org.miradi.schemas.ProjectMetadataSchema;
import org.miradi.schemas.TncProjectDataSchema;

import javax.swing.*;

public class SummaryProjectPanel extends ObjectDataInputPanel
{
	public SummaryProjectPanel(MainWindow mainWindow, ORef refToUse) throws Exception
	{
		super(mainWindow.getProject(), refToUse);
		
		addField(createStringField(ProjectMetadata.TAG_PROJECT_NAME));
		
		addMiradiShareField();
		addField(createChoiceField(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_PROJECT_LANGUAGE, new MajorLanguagesQuestion()));
		addField(createDateChooserField(ProjectMetadata.TAG_DATA_EFFECTIVE_DATE));
		addField(createReadonlyTextField(ProjectMetadata.PSEUDO_TAG_PROJECT_FILENAME));
		addField(createExternalProjectIdsDisplayField(ProjectMetadata.TAG_XENODATA_STRING_REF_MAP));
		addBlankHorizontalLine();
		
		addField(createShortStringField(ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER));
		addField(createStringField(ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS));
		addField(createStringField(ProjectMetadata.TAG_PROJECT_URL));
		addField(createMultilineField(ProjectMetadata.TAG_PROJECT_DESCRIPTION));

		addTaxonomyFields(MiradiShareProjectDataSchema.getObjectType());

		createLatestExtendedProgressReportFields(ObjectType.PROJECT_METADATA);

		setObjectRefs(new ORef[]{refToUse, getProject().getSingletonObjectRef(TncProjectDataSchema.getObjectType()), getProject().getSingletonObjectRef(MiradiShareProjectDataSchema.getObjectType()),});
		updateFieldsFromProject();
	}

	private void addMiradiShareField() throws Exception
	{
		String htmlLinkToMiradiShareTab = "<a href=\"\">" + EAM.text("Learn about Miradi Share") + "</a>";
		final String label = EAM.text("Miradi Share Program");
		if (getProject().getMetadata().isMiradiShareProject())
		{
			final String tabIdentifier = SummaryMiradiSharePanel.class.getSimpleName();
			final ObjectDataField clickableLinkField = createStaticReadonlyClickableLinkField(ProjectMetadataSchema.getObjectType(), htmlLinkToMiradiShareTab, tabIdentifier);
			final ObjectDataField readonlyProgramNameField = createReadonlyTextField(ProjectMetadataSchema.getObjectType(), ProjectMetadata.PSEUDO_TAG_MIRADI_SHARE_PROGRAM_NAME);
			addFieldsOnOneLineWithoutFieldLabels(label, new ObjectDataField[]{readonlyProgramNameField, clickableLinkField});
		}
		else
		{
			final String tabIdentifier = SummaryNonSharedMiradiSharePanel.class.getSimpleName();
			final ObjectDataField staticReadonlyClickableLinkField = createStaticReadonlyClickableLinkField(ProjectMetadataSchema.getObjectType(), htmlLinkToMiradiShareTab, tabIdentifier);
			addFieldsOnOneLineWithoutFieldLabels(label, new ObjectDataField[]{staticReadonlyClickableLinkField, });
		}
	}

	@Override
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	@Override
	public Icon getIcon()
	{
		return new MiradiApplicationIcon();
	}
	
	@Override
	public Class getJumpActionClass()
	{
		return ActionViewSummary.class;
	}
	
	@Override
	public boolean isRtfExportable()
	{
		return true;
	}
	
	@Override
	public void exportRtf(RtfWriter writer) throws Exception
	{
		RtfFormExporter rtfFormExporter = new RtfFormExporter(getProject(), writer, getSelectedRefs());
		rtfFormExporter.exportForm(new ProjectTabForm());
	}
	
	public static final String PANEL_DESCRIPTION = EAM.text("Summary");
}
