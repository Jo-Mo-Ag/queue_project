package com.jomo.queue_project;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;

public class GUIController {

	@FXML
	private ComboBox<String> modelsComboBox;

	@FXML
	private Label muLable;

	@FXML
	private TextField muTextField;

	@FXML
	private Label lambdaLable;

	@FXML
	private Label showInputLable;

	@FXML
	private TextField lambdaTextField;

	@FXML
	private Label kLable;

	@FXML
	private TextField kTextField;


	@FXML
	private Label cLable;

	@FXML
	private TextField cTextField;

	@FXML
	private Label errorLabel;

	@FXML
	private WebView resultWebView;

	final static ObservableList<String>models=
			FXCollections.observableArrayList
			("D/D/1","D/D/1/K","M/M/1","M/M/1/K","M/M/C","M/M/C/K");
	private String model=models.get(0);
	private SolvingModels solvingModels;
	private Pattern pattern;//pattern to match partial number
	private String pageFormate = "<html><head>"
			+ "<style>body{ font-size:25;background-color: #04A997;}"
			+ "</style></head>"
			+ "<body>%s"
			+ "</body></html>";
	private double lambda,mu;
	private int k,c;
	public void initialize() {
		solvingModels=new SolvingModels();
		pattern=Pattern.compile("(\\d+)/(\\d+)");// 1/2
		modelsComboBox.setItems(models);
		modelsComboBox.getSelectionModel().selectFirst();
		errorLabel.setManaged(false);
		setComponentVisable(0);
		modelsComboBox.getSelectionModel().selectedItemProperty()
		.addListener( (options, oldValue, newValue) -> {
			model=newValue;
			int index=models.indexOf(newValue);
			setComponentVisable(index);

		}
				); 
		resultWebView.getEngine().loadContent(pageFormate("the result will show here"));


	}

	private void setComponentVisable(int indexOf) {
		if(indexOf==5||indexOf==4) {
			cLable.setVisible(true);
			cLable.setManaged(true);
			cTextField.setVisible(true);
			cTextField.setManaged(true);
		}else {
			cLable.setVisible(false);
			cLable.setManaged(false);;
			cTextField.setVisible(false);
			cTextField.setManaged(false);
		}
		switch (indexOf) {
		case 1:
		case 3:
		case 5:
			kLable.setVisible(true);
			kLable.setManaged(true);;
			kTextField.setVisible(true);
			kTextField.setManaged(true);
			break;

		default:
			kLable.setVisible(false);
			kLable.setManaged(false);;
			kTextField.setVisible(false);
			kTextField.setManaged(false);
			break;
		}
		setLambdaAndMuLabelText(indexOf);
		setInputLableText(indexOf);
	}

	private void setLambdaAndMuLabelText(int indexOf) {
		//mm models
		if(indexOf==2||indexOf==3||indexOf==4||indexOf==5) {
			lambdaLable.setText("arrival avarage rate(λ):");
			muLable.setText("service avarage rate(μ)");
		}
		//dd models
		else {
			lambdaLable.setText("interarrival time(1/λ):");
			muLable.setText("Service time(1/μ):");
		}
	}

	private void setInputLableText(int indexOf) {
		if(indexOf==1||indexOf==3) {
			showInputLable.setText("λ:             μ:             k:");
		}
		else if(indexOf==4) {
			showInputLable.setText("λ:             μ:             c:");
		}else if(indexOf==5) {
			showInputLable.setText("λ:             μ:             c:             k:");
		}
		else {
			showInputLable.setText("λ:             μ:");

		}
	}

	@FXML
	void showResultButtonClicked(ActionEvent event) {
		errorLabel.setManaged(false);
		errorLabel.setVisible(false);
		int index=models.indexOf(model);
		switch(index) {
		case 0:
			DD1Model();
			break;
		case 1: 
			DD1KModel();
			break;
		case 2:MM1Model();
		break;
		case 3:MM1KModel();
		break;
		case 4:MMCModel();
		break;
		case 5:MMCKModel();
		break;
		}

	}






	private void DD1Model() {

		try {
			int lambda=Integer.parseInt(lambdaTextField.getText());
			int mu=Integer.parseInt(muTextField.getText());
			vaildInt(lambda);
			vaildInt(mu);
		
			showInputLable.setText("λ:1/"+lambda+"             μ:1/"+mu);
			String r=solvingModels.solveDD1Model(mu, lambda);



			resultWebView.getEngine().loadContent(pageFormate(r));

		} catch (Exception e) {
			errorModelWithoutK();

		}

	}
	private void DD1KModel() {

		try {
			int lambda=Integer.parseInt(lambdaTextField.getText());
			int mu=Integer.parseInt(muTextField.getText());
			int k=Integer.parseInt(kTextField.getText());;
			vaildInt(lambda);
			vaildInt(mu);
			vaildInt(k);
			

			showInputLable.setText("λ:1/"+lambda+"             μ:1/"+mu+"             k:"+k);

			String r=solvingModels.solveDD1KModel(mu, lambda, k);

			resultWebView.getEngine().loadContent(pageFormate(r));

		} catch (Exception e) {
			erroInModelHasK();


		}

	}



	private void erroInModelHasK() {
		errorLabel.setManaged(true);
		errorLabel.setVisible(true);
		setInputLableText(models.indexOf(model));
		resultWebView.getEngine().loadContent(pageFormate("the result will show here"));
	}
	private void MM1Model() {

		try {
			String lamb=lambdaTextField.getText();

			String sMu=muTextField.getText();
			readLambdaAndMu();
			
			showInputLable.setText("λ:"+lamb+"             μ:"+sMu);

			String r=solvingModels.solveMM1Model(mu, lambda);

			resultWebView.getEngine().loadContent(pageFormate(r));

		} catch (Exception e) {
			errorModelWithoutK();

		}
	}



	private void errorModelWithoutK() {
		errorLabel.setManaged(true);
		errorLabel.setVisible(true);
		setInputLableText(models.indexOf(model));
		resultWebView.getEngine().loadContent(pageFormate("the result will show here"));
	}

	private void MM1KModel() {

		try {
			String lamb=lambdaTextField.getText();

			String sMu=muTextField.getText();
			readLamdaAndMuAndK();
			

			showInputLable.setText("λ:"+lamb+"             μ:"+sMu+"             k:"+k);

			String r=solvingModels.solveMM1KModel(mu, lambda, k);

			resultWebView.getEngine().loadContent(pageFormate(r));

		} catch (Exception e) {
			erroInModelHasK();
		}
	}
	private void MMCModel() {
		try {
			String lamb=lambdaTextField.getText();

			String sMu=muTextField.getText();
			readLamdaAndMuAndC();
			

			showInputLable.setText("λ:"+lamb+"             μ:"+sMu+"             c:"+c);

			String r=solvingModels.solveMMCModel(mu, lambda, c);

			resultWebView.getEngine().loadContent(pageFormate(r));

		} catch (Exception e) {
			erroInModelHasK();
		}

	}
	private void MMCKModel() {
		try {
			String lamb=lambdaTextField.getText();

			String sMu=muTextField.getText();
			readLamdaAndMuAndKAndC();
			

			showInputLable.setText("λ:"+lamb+"             μ:"+sMu+"             c:"+c+"             k:"+k);

			String r=solvingModels.solveMMCKModel(mu, lambda, c,k);

			resultWebView.getEngine().loadContent(pageFormate(r));

		} catch (Exception e) {
			erroInModelHasK();
		}

	}
	private void readLambdaAndMu() throws Exception {
		String lamb=lambdaTextField.getText();
		Matcher m=pattern.matcher(lamb);// 1/3

		if(m.matches()) {
			lambda=parseDouble(m);
		}else {
			lambda=Double.parseDouble(lamb);
		}
		String sMu=muTextField.getText();
		m=pattern.matcher(sMu);// 2/7

		if(m.matches()) {
			mu=parseDouble(m);
		}else {
			mu=Double.parseDouble(sMu);
		}
		vaildDouble(lambda);
		vaildDouble(mu);
	}
	private void readLamdaAndMuAndK() throws Exception {
		readLambdaAndMu();
		k=Integer.parseInt(kTextField.getText());
		vaildInt(k);

	}
	private void readLamdaAndMuAndC() throws Exception {
		readLambdaAndMu();
		c=Integer.parseInt(cTextField.getText());
		vaildInt(c);		
	}
	private void readLamdaAndMuAndKAndC() throws Exception {
		readLambdaAndMu();
		c=Integer.parseInt(cTextField.getText());
		k=Integer.parseInt(kTextField.getText());
		vaildInt(k);
		vaildInt(c);		
	}

	private void vaildInt(int num) throws Exception {
		if(num<=0)throw  new Exception("number is less than zero");

	}
	private void vaildDouble(double num) throws Exception {
		if(num<=0.0d) throw  new Exception("number is less than zero");

	}
	private String pageFormate(String result) {

		return String.format(pageFormate, result);

	}
	private double parseDouble(Matcher m) {
		double  numerator=Double.parseDouble(m.group(1)); // (1)/(10)
		int denominator =Integer.parseInt(m.group(2));
		if(denominator==0)throw new ArithmeticException();
		return numerator/denominator;
	}

}
