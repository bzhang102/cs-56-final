package com.example.cs56final;

import java.util.ArrayList;

import java.util.List;
import javafx.application.Application;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView.EditEvent;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class OrgChartView extends Application
{
	private final TreeView<String> treeView = new TreeView<>();
	private final TextArea textArea = new TextArea();
	private TextField titleTextField = new TextField();
	private TextField firstNameTextField = new TextField();
	private TextField lastNameTextField = new TextField();
	private TextField searchTextField = new TextField();

	public static void main(String[] args)
	{
		Application.launch(args);
	}

	@Override
	public void start(Stage stage)
	{
		OrgChartInitializer helper = new OrgChartInitializer();
		ArrayList<TreeItem<String>> employees = helper.getEmployees();
		treeView.setEditable(true);
		treeView.setCellFactory(TextFieldTreeCell.forTreeView());

		treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		treeView.getSelectionModel().selectFirst();
		TreeItem<String> rootEmployee = new TreeItem<>("President, Brandon Zhang");
		rootEmployee.getChildren().addAll(employees);
		treeView.setRoot(rootEmployee);

		// Set editing event handlers
		treeView.setOnEditStart(new EventHandler<TreeView.EditEvent<String>>()
		{
			@Override
			public void handle(EditEvent<String> event)
			{
				editStart(event);
			}
		});
		treeView.setOnEditCommit(new EventHandler<TreeView.EditEvent<String>>()
		{
			@Override
			public void handle(EditEvent<String> event)
			{
				editCommit(event);
			}
		});
		treeView.setOnEditCancel(new EventHandler<TreeView.EditEvent<String>>()
		{
			@Override
			public void handle(EditEvent<String> event)
			{
				editCancel(event);
			}
		});

		// Set tree modification related event handlers
		rootEmployee.addEventHandler(TreeItem.<String>branchExpandedEvent(),new EventHandler<TreeItem.TreeModificationEvent<String>>()
		{
			@Override
			public void handle(TreeModificationEvent<String> event)
			{
				branchExpended(event);
			}
		});
		rootEmployee.addEventHandler(TreeItem.<String>branchCollapsedEvent(),new EventHandler<TreeItem.TreeModificationEvent<String>>()
		{
			@Override
			public void handle(TreeModificationEvent<String> event)
			{
				branchCollapsed(event);
			}
		});
		rootEmployee.addEventHandler(TreeItem.<String>childrenModificationEvent(),new EventHandler<TreeItem.TreeModificationEvent<String>>()
		{
			@Override
			public void handle(TreeModificationEvent<String> event)
			{
				childrenModification(event);
			}
		});


		// Layout
		VBox rightPane = getRightPane();
		HBox root = new HBox();
		root.setSpacing(20);
		root.getChildren().addAll(treeView,rightPane);

		Scene scene = new Scene(root,800,600);
		stage.setScene(scene);
		stage.setTitle("Organization Chart");
		stage.show();
	}

	private VBox getRightPane()
	{
		Button addEmployeeButton = new Button("Add Employee");
		addEmployeeButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				addEmployee(titleTextField.getText(), firstNameTextField.getText(), lastNameTextField.getText());
			}
		});

		// Create the searchEmployeeButton and its corresponding Event Handler
		Button searchEmployeeButton = new Button("Search Employee");
		searchEmployeeButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				String searchFor = searchTextField.getText();
				writeMessage("Search for: " + searchFor);

				if (searchFor == null || searchFor.trim().length() == 0) {
					writeMessage("Please provide at least 1 character to search");
					return;
				}

				treeView.getSelectionModel().clearSelection();
				collapseTreeView(treeView.getRoot());
				ArrayList<TreeItem> foundChild = new ArrayList<>();
				ArrayList<TreeItem> found = searchEmployee(treeView.getRoot(), searchFor, foundChild);
				writeMessage(found.size() + " employees found.");

				// we have to select the child first, otherwise the treeview is not showing correctly
				for (int i = found.size(); i >0; i --) {
					treeView.getSelectionModel().select(found.get(i - 1));
				}
			}
		});

		// Create the removeEmployeeButton and its corresponding Event Handler
		Button removeEmployeeButton = new Button("Remove Employee");
		removeEmployeeButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				removeEmployee();
			}
		});

		textArea.setPrefRowCount(15);
		textArea.setPrefColumnCount(25);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(12);

		HBox buttonHBox = new HBox();
		buttonHBox.setSpacing(10.0);
		buttonHBox.getChildren().addAll(addEmployeeButton, searchEmployeeButton, removeEmployeeButton);

		grid.add(new Label("Title"), 0, 0);
		grid.add(titleTextField, 1, 0);
		grid.add(new Label("First Name"), 0, 1);
		grid.add(firstNameTextField, 1, 1);
		grid.add(new Label("Last Name"), 0, 2);
		grid.add(lastNameTextField, 1, 2);
		grid.add(new Label("Search"), 0, 3);
		grid.add(searchTextField, 1, 3);

		VBox employeeVBox = new VBox();
		employeeVBox.getChildren().addAll(grid, buttonHBox, new Label("Message Log:"), textArea);
		employeeVBox.setSpacing(10);

		return employeeVBox;
	}

	// Helper Method for Adding an Employee
	private void addEmployee(String title, String firstName, String lastName)
	{
		if (title == null || title.trim().length() == 0
				|| firstName == null || firstName.trim().length() == 0
				|| lastName == null || lastName.trim().length() == 0
		)
		{
			this.writeMessage("Employee cannot be empty.");
			return;
		}

		List<TreeItem<String>> selectedItems = treeView.getSelectionModel().getSelectedItems();

		TreeItem<String> parent;
		if (selectedItems == null || selectedItems.size() != 1)
		{
			this.writeMessage("Select one and only one employee to add to.");
			return;
		} else {
			 parent = selectedItems.get(0);
		}

		TreeItem<String> newEmployee = new TreeItem<String>(title + ", " + firstName + " " + lastName);
		parent.getChildren().add(newEmployee);

		newEmployee.setExpanded(true);
		treeView.getSelectionModel().clearSelection();
		treeView.getSelectionModel().select(newEmployee);
	}

	// Helper Method for Removing an Employee
	private void removeEmployee()
	{
		List<TreeItem<String>> selectedItems = treeView.getSelectionModel().getSelectedItems();

		TreeItem<String> item;
		if (selectedItems == null || selectedItems.size() != 1)
		{
			this.writeMessage("Select one and only one employee to remove.");
			return;
		} else {
			item = selectedItems.get(0);
		}

		TreeItem<String> parent = item.getParent();
		if (parent == null )
		{
			this.writeMessage("Cannot remove the root employee.");
		}
		else
		{
			for(TreeItem<String> child : item.getChildren())
			{
				parent.getChildren().add(child);
			}
			parent.getChildren().remove(item);
		}
	}

	private ArrayList<TreeItem> searchEmployee(TreeItem current, String searchFor, ArrayList<TreeItem> foundChild)
	{
		String value = String.valueOf(current.getValue());

		if (value.contains(searchFor)) {
			foundChild.add(current);
		}

		if(!current.isLeaf()) {
			for (Object child : current.getChildren()) {
				searchEmployee((TreeItem)child, searchFor, foundChild);
			}
		}

		return foundChild;
	}

	private void collapseTreeView(TreeItem<?> item){
		if(item != null && !item.isLeaf()){
			item.setExpanded(false);
			for(TreeItem<?> child:item.getChildren()){
				collapseTreeView(child);
			}
		}
	}

	// Helper Methods for the Event Handlers
	private void branchExpended(TreeItem.TreeModificationEvent<String> event)
	{
		String nodeValue = event.getSource().getValue().toString();
		this.writeMessage("Employee " + nodeValue + " expanded.");
	}

	private void branchCollapsed(TreeItem.TreeModificationEvent<String> event)
	{
		String nodeValue = event.getSource().getValue().toString();
		this.writeMessage("Employee " + nodeValue + " collapsed.");
	}

	private void childrenModification(TreeItem.TreeModificationEvent<String> event)
	{
		if (event.wasAdded())
		{
			for(TreeItem<String> item : event.getAddedChildren())
			{
				this.writeMessage("Employee " + item.getValue() + " has been added.");
			}
		}

		if (event.wasRemoved())
		{
			for(TreeItem<String> item : event.getRemovedChildren())
			{
				this.writeMessage("Employee " + item.getValue() + " has been removed.");
			}
		}
	}

	private void editStart(TreeView.EditEvent<String> event)
	{
		//this.writeMessage("Started editing: " + event.getTreeItem() );
	}

	private void editCommit(TreeView.EditEvent<String> event)
	{
		this.writeMessage(event.getTreeItem() + " changed." +
				" old = " + event.getOldValue() +
				", new = " + event.getNewValue());
	}

	private void editCancel(TreeView.EditEvent<String> e)
	{
		//this.writeMessage("Cancelled editing: " + e.getTreeItem() );
	}

	// Method for Logging
	private void writeMessage(String msg)
	{
		this.textArea.appendText(msg + "\n");
	}
}
