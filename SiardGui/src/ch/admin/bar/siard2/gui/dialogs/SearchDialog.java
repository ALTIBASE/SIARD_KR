/*
 * ====================================================================== SearchDialog asks for a
 * string to find in the table data. Application : Siard2 Description : SearchDialog asks for a
 * string to find in the table data. Platform : Java 7, JavaFX 2.2
 * ------------------------------------------------------------------------ Copyright : 2017, Enter
 * AG, Rüti ZH, Switzerland Created : 31.08.2017, Hartwig Thomas
 * ======================================================================
 */
package ch.admin.bar.siard2.gui.dialogs;

import java.util.List;

import org.apache.log4j.Logger;

import ch.admin.bar.siard2.api.MetaColumn;
import ch.admin.bar.siard2.api.MetaTable;
import ch.admin.bar.siard2.gui.SiardBundle;
import ch.enterag.utils.fx.FxSizes;
import ch.enterag.utils.fx.FxStyles;
import ch.enterag.utils.fx.ScrollableDialog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/* ==================================================================== */
/**
 * SearchDialog asks for a string to find in the table data.
 *
 * @author Hartwig Thomas
 */
public class SearchDialog
	extends ScrollableDialog
	implements EventHandler<ActionEvent>
{
	private static final Logger LOG = Logger.getLogger(SearchDialog.class);

	private static final double dINNER_PADDING = 10.0;
	private static final double dVSPACING = 10.0;
	private static final double dHSPACING = 10.0;
	private static final int iTEXT_COLUMNS = 32;
	private boolean _bCanceled = true; // just closing with the close button is canceling

	public boolean isCanceled()
	{
		return _bCanceled;
	}

	private TextField _tfFindString = null;

	public String getFindString()
	{
		return _tfFindString.getText();
	}

	private CheckBox _cbMatchCase = null;

	public boolean mustMatchCase()
	{
		return _cbMatchCase.isSelected();
	}

	private MetaColumnsVBox _mcvb = null;

	public List<MetaColumn> getSelection()
	{
		return _mcvb.getSelection();
	}

	private Button _btnOk = null;
	private Button _btnCancel = null;

	private void disableOk()
	{
		_btnOk.setDisable((getFindString().length() == 0) || (getSelection().size() == 0));
	} /* disableOk */

	/* ================================================================== */
	private class BooleanChangeListener
		implements ChangeListener<Boolean>
	{
		/*------------------------------------------------------------------*/
		/**
		 * react to change in selection of columns.
		 */
		@Override
		public void changed(ObservableValue<? extends Boolean> ovb,
			Boolean bSelectedOld, Boolean bSelectedNew)
		{
			disableOk();
		} /* changed */
	} /* class BooleanChangeListener */

	private BooleanChangeListener _bcl = new BooleanChangeListener();

	/* ================================================================== */
	private class StringChangeListener
		implements ChangeListener<String>
	{
		/**
		 * react to change in find string.
		 */
		@Override
		public void changed(ObservableValue<? extends String> ovs,
			String sOld, String sNew)
		{
			disableOk();
		} /* changed */
	} /* class StringChangeListener */

	private StringChangeListener _scl = new StringChangeListener();

	/*------------------------------------------------------------------*/
	/**
	 * handle pressed button.
	 *
	 * @param ae action event.
	 */
	@Override
	public void handle(ActionEvent ae)
	{
		if (ae.getSource() == _btnOk)
			_bCanceled = false;
		close();
	} /* handle */

	/*------------------------------------------------------------------*/
	/**
	 * create a HBox with OK and Cancel button.
	 *
	 * @param sOk text on OK button.
	 * @param sCancel text on Cancel button.
	 * @return HBox.
	 */
	private HBox createButtonsHBox(String sOk, String sCancel)
	{
		HBox hbox = new HBox();
		hbox.setSpacing(dHSPACING);
		hbox.setStyle(FxStyles.sSTYLE_BACKGROUND_LIGHTGREY);
		hbox.setAlignment(Pos.BASELINE_RIGHT);
		/* buttons and their width */
		_btnOk = new Button(sOk);
		_btnOk.setDefaultButton(true);
		_btnOk.setOnAction(this);

		_btnOk.setDisable(true); /* IntraDIGM */

		hbox.getChildren().add(_btnOk);
		_btnCancel = new Button(sCancel);
		_btnCancel.setCancelButton(true); // associate it with ESC key
		_btnCancel.setOnAction(this);
		hbox.getChildren().add(_btnCancel);
		return hbox;
	} /* createButtonsHBox */

	/*------------------------------------------------------------------*/
	/**
	 * create the columns VBox
	 *
	 * @param sb string pool.
	 * @param table table to be searched.
	 * @return columns VBox with all/none button.
	 */
	private VBox createColumnsVBox(SiardBundle sb, MetaTable mt)
	{
		_mcvb = new MetaColumnsVBox(sb.getSearchExplanation(), mt, sb.getSearchSelectAll(), sb.getSearchSelectNone());
		_mcvb.addSelectionChangeListener(_bcl);
		return _mcvb;
	} /* createColumnsVBox */

	/*------------------------------------------------------------------*/
	/**
	 * create HBox with label and check box for match case.
	 *
	 * @param sMatchCaseLabel label.
	 * @param dLabelWidth width of label.
	 * @param bMatchCase initial value of match case.
	 * @return HBox with label and check box.
	 */
	private HBox createMatchCaseHBox(String sMatchCaseLabel, double dLabelWidth,
		boolean bMatchCase)
	{
		HBox hbox = new HBox();
		hbox.setSpacing(dHSPACING);
		hbox.setStyle(FxStyles.sSTYLE_BACKGROUND_LIGHTGREY);
		_cbMatchCase = new CheckBox();
		_cbMatchCase.setSelected(bMatchCase);
		Label lblMatchCase = new Label(sMatchCaseLabel);
		lblMatchCase.setMinHeight(FxSizes.getNodeHeight(_cbMatchCase)); /* 2020-08-31 */
		lblMatchCase.setLabelFor(_cbMatchCase);
		lblMatchCase.setAlignment(Pos.BASELINE_RIGHT);
		hbox.getChildren().add(lblMatchCase);
		hbox.getChildren().add(_cbMatchCase);
		return hbox;
	} /* createMatchCaseHBox */

	/*------------------------------------------------------------------*/
	/**
	 * create HBox with label and text field for find string.
	 *
	 * @param sFindStringLabel label.
	 * @param dLabelWidth width of label.
	 * @param sFindString initial value of find string.
	 * @return HBox with label and text field.
	 */
	private HBox createFindStringHBox(String sFindStringLabel, double dLabelWidth,
		String sFindString)
	{
		HBox hbox = new HBox();
		hbox.setSpacing(dHSPACING);
		hbox.setStyle(FxStyles.sSTYLE_BACKGROUND_LIGHTGREY);
		_tfFindString = new TextField();
		if (sFindString != null)
			_tfFindString.setText(sFindString);
		_tfFindString.setMinWidth(FxSizes.fromEms(iTEXT_COLUMNS));
		_tfFindString.textProperty().addListener(_scl);

		HBox.setHgrow(_tfFindString, Priority.ALWAYS);

		Label lblFindString = new Label(sFindStringLabel);
		lblFindString.setMinWidth(dLabelWidth);
		lblFindString.setMinHeight(FxSizes.getNodeHeight(_tfFindString)); /* 2020-08-31 */
		lblFindString.setLabelFor(_tfFindString);
		lblFindString.setAlignment(Pos.BASELINE_RIGHT);
		hbox.getChildren().add(lblFindString);
		hbox.getChildren().add(_tfFindString);
		return hbox;
	} /* createFindStringHBox */

	/*------------------------------------------------------------------*/
	/**
	 * create the main VBox of the dialog.
	 *
	 * @param sb string pool.
	 * @param mt meta data of table to be searched.
	 * @param sFindString initial find string.
	 * @param bMatchCase initial match case value.
	 * @return VBox.
	 */
	private VBox createVBox(SiardBundle sb, MetaTable mt, String sFindString, boolean bMatchCase)
	{
		VBox vbox = new VBox();

		// vbox.setPadding(new Insets(dINNER_PADDING)); /* IntraDIGM */
		vbox.setPadding(new Insets(dOUTER_PADDING * 2, dOUTER_PADDING, 0, dOUTER_PADDING)); /* IntraDIGM */

		vbox.setSpacing(dVSPACING);
		vbox.setStyle(FxStyles.sSTYLE_BACKGROUND_LIGHTGREY);

		String sFindStringLabel = sb.getFindStringLabel();
		String sMatchCaseLabel = sb.getFindMatchCaseLabel();

		double dLabelWidth = FxSizes.getTextWidth(sFindStringLabel);
		if (dLabelWidth < FxSizes.getTextWidth(sMatchCaseLabel))
			dLabelWidth = FxSizes.getTextWidth(sMatchCaseLabel);

		vbox.getChildren().add(createFindStringHBox(sFindStringLabel, dLabelWidth, sFindString));
		vbox.getChildren().add(createMatchCaseHBox(sMatchCaseLabel, dLabelWidth, bMatchCase));
		vbox.getChildren().add(new Separator());
		vbox.getChildren().add(this.createColumnsVBox(sb, mt));
		vbox.getChildren().add(new Separator());
		vbox.getChildren().add(createButtonsHBox(sb.getOk(), sb.getCancel()));

		vbox.setMinWidth(FxSizes.getNodeWidth(vbox));
		vbox.setMinHeight(FxSizes.getNodeHeight(vbox));
		return vbox;
	} /* createVBox */

	/*------------------------------------------------------------------*/
	/**
	 * constructor
	 *
	 * @param stageOwner owner window.
	 * @param mt meta data of table to be searched.
	 * @param sFindString initial find string value.
	 * @param bMatchCase initial match case value.
	 */
	private SearchDialog(Stage stageOwner, MetaTable mt, String sFindString, boolean bMatchCase)
	{
		super(stageOwner, SiardBundle.getSiardBundle().getSearchTitle(mt.getName()));
		SiardBundle sb = SiardBundle.getSiardBundle();
		VBox vbox = createVBox(sb, mt, sFindString, bMatchCase);
		/* scene */
		//Scene scene = new Scene(vbox, vbox.getMinWidth() + 10.0, vbox.getMinHeight() + 10.0);
		Scene scene = new Scene(vbox);
		setScene(scene);
	} /* constructor */

	/*------------------------------------------------------------------*/
	/**
	 * shows search dialog.
	 *
	 * @param stageOwner owner window.
	 * @param mt meta data of table to be searched.
	 * @param sFindString initial find string value.
	 * @param bMatchCase initial match case value.
	 * @return search dialog with result.
	 */
	public static SearchDialog showSearchDialog(Stage stageOwner,
		MetaTable mt, String sFindString, boolean bMatchCase)
	{
		SearchDialog sd = new SearchDialog(stageOwner, mt, sFindString, bMatchCase);

		sd.setResizable(false); /* IntraDIGM */

		sd.showAndWait();
		return sd;
	} /* showSearchDialog */

} /* SearchDialog */
