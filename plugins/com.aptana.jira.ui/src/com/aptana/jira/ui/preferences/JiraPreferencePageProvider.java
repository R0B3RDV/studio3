/**
 * Aptana Studio
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.jira.ui.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.aptana.core.util.StringUtil;
import com.aptana.jira.core.JiraCorePlugin;
import com.aptana.jira.core.JiraException;
import com.aptana.jira.core.JiraManager;
import com.aptana.jira.core.JiraUser;
import com.aptana.ui.preferences.AbstractAccountPageProvider;
import com.aptana.ui.util.SWTUtils;
import com.aptana.ui.util.WorkbenchBrowserUtil;

/**
 * @author Michael Xia (mxia@appcelerator.com)
 */
public class JiraPreferencePageProvider extends AbstractAccountPageProvider
{

	private static final String SIGNUP_URL = "https://jira.appcelerator.org/secure/Signup!default.jspa"; //$NON-NLS-1$

	private Group main;
	private Text usernameText;
	private Text passwordText;
	private Button testButton;
	private Button createAccountButton;

	public JiraPreferencePageProvider()
	{
	}

	public Control createContents(Composite parent)
	{
		main = new Group(parent, SWT.NONE);
		main.setText(Messages.JiraPreferencePageProvider_LBL_Jira);
		main.setLayout(GridLayoutFactory.swtDefaults().numColumns(3).create());

		Label label = new Label(main, SWT.NONE);
		label.setText(StringUtil.makeFormLabel(Messages.JiraPreferencePageProvider_LBL_Username));
		label.setLayoutData(GridDataFactory.swtDefaults().create());

		usernameText = new Text(main, SWT.BORDER);
		usernameText
				.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).create());

		testButton = new Button(main, SWT.NONE);
		testButton.setText(Messages.JiraPreferencePageProvider_LBL_Validate);
		testButton.setLayoutData(GridDataFactory.swtDefaults().hint(getButtonWidthHint(testButton), SWT.DEFAULT)
				.create());
		testButton.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				validate();
			}
		});

		label = new Label(main, SWT.NONE);
		label.setText(StringUtil.makeFormLabel(Messages.JiraPreferencePageProvider_LBL_Password));
		label.setLayoutData(GridDataFactory.swtDefaults().create());

		passwordText = new Text(main, SWT.BORDER | SWT.PASSWORD);
		passwordText
				.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).create());

		createAccountButton = new Button(main, SWT.NONE);
		createAccountButton.setText(StringUtil.ellipsify(Messages.JiraPreferencePageProvider_LBL_Signup));
		createAccountButton.setLayoutData(GridDataFactory.swtDefaults()
				.hint(getButtonWidthHint(createAccountButton), SWT.DEFAULT).create());
		createAccountButton.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				WorkbenchBrowserUtil.launchExternalBrowser(SIGNUP_URL);
			}
		});

		adjustWidth();

		// loads the existing user
		JiraUser user = getJiraManager().getUser();
		if (user != null)
		{
			usernameText.setText(user.getUsername());
			passwordText.setText(user.getPassword());
		}

		return main;
	}

	public boolean performOk()
	{
		return validate();
	}

	protected JiraManager getJiraManager()
	{
		return JiraCorePlugin.getDefault().getJiraManager();
	}

	private void adjustWidth()
	{
		List<Control> actionControls = new ArrayList<Control>();
		actionControls.add(testButton);
		actionControls.add(createAccountButton);

		SWTUtils.resizeControlWidthInGrid(actionControls);
	}

	private boolean validate()
	{
		String username = usernameText.getText();
		String password = passwordText.getText();
		if (StringUtil.isEmpty(username))
		{
			MessageDialog.openError(main.getShell(), Messages.JiraPreferencePageProvider_ERR_InvalidInput_Title,
					Messages.JiraPreferencePageProvider_ERR_EmptyUsername);
			return false;
		}
		if (StringUtil.isEmpty(password))
		{
			MessageDialog.openError(main.getShell(), Messages.JiraPreferencePageProvider_ERR_InvalidInput_Title,
					Messages.JiraPreferencePageProvider_ERR_EmptyPassword);
			return false;
		}
		try
		{
			getJiraManager().login(username, password);
		}
		catch (JiraException e)
		{
			MessageDialog.openError(main.getShell(), Messages.JiraPreferencePageProvider_ERR_LoginFailed_Title,
					e.getMessage());
			return false;
		}
		return true;
	}

	private static int getButtonWidthHint(Button button)
	{
		PixelConverter converter = new PixelConverter(button);
		int widthHint = converter.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
	}
}
