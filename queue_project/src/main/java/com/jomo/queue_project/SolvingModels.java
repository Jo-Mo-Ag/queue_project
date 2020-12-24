package com.jomo.queue_project;

import java.text.DecimalFormat;
import static java.lang.Math.pow;
public class SolvingModels {

	private String seperator="<br><br>";
	private String space="&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";
	private String Wq="W"+subscript("q")+"(n)=";

	private DecimalFormat df = new DecimalFormat("#.###"); 


	public String solveDD1Model(int mu, int lambda) {
		StringBuilder builder=new StringBuilder();
		if(mu==lambda)builder.append("n(t)=1");
		else
			builder.append("n(t)=t/"+lambda+" - (t-"+lambda+")/"+mu);

		builder.append(seperator);
		if(mu>lambda) {
			builder.append(Wq+(mu-lambda)+"*(n-1)");

		}else {
			builder.append(Wq+0);
		}
		return builder.toString();
	}

	public String solveDD1KModel(int mu, int lambda, int k) {
		StringBuilder builder=new StringBuilder();
		if(lambda>=mu) {
			if(lambda==mu) {
				builder.append("n(t)=1");
			}else {
				builder.append("n(t)=1"+space+"if t%"+lambda+"<="+mu);
				builder.append(seperator);
				builder.append("n(t)=0"+space+"if t%"+lambda+">"+mu);
			}

			builder.append(seperator);
			builder.append(Wq+0);

		}else {
			int ti=calculateTi(mu, lambda, k);
			builder.append("t"+subscript("i")+"="+ti);
			builder.append(seperator);
			builder.append("n(t)=0"+space+"if t<"+lambda);
			builder.append(seperator);
			builder.append("n(t)=⌊t/"+lambda+"⌋- ⌊(t-"+lambda+")/"+mu+"⌋"
					+ space+"if "+lambda+"&ltt<="+ti);
			builder.append(seperator);
			builder.append("n(t)="+(k-1)+" or "+(k-2)+space+"if t>"+ti);
			builder.append(seperator);
			builder.append(Wq+"0"+space+"if n=0");
			builder.append(seperator);
			builder.append(Wq+(mu-lambda)+"*(n-1)"+space+"if n<"+(ti/lambda));
			builder.append(seperator);
			builder.append(Wq+
					(mu-lambda)*((ti/lambda)-2)+" or "+
					(mu-lambda)*((ti/lambda)-3)+space+"if n>="+(ti/lambda));


		}
		return builder.toString();
	}
	public int calculateTi(int mu,int lambda,int k) {

		int ti= (k*mu*lambda-lambda*lambda)/(mu-lambda);
		
		while(isMinimumTi(ti-lambda,mu,lambda,k)) {
			
			ti-=lambda;
		}

		return ti;
	}


	public boolean isMinimumTi(int ti, int mu, int lambda, int k) {
		// TODO Auto-generated method stub
		return k==ti/lambda-(ti-lambda)/mu;

	}
	public String solveMM1Model(double mu, double lambda) throws Exception {
		// TODO Auto-generated method stub
		//if(mu<=lambda)throw new Exception();
		StringBuilder builder=new StringBuilder();
		double w=1/(mu-lambda);
		double wq=lambda/(mu*(mu-lambda));
		double l=lambda*w;
		double lq=lambda*wq;
		builder.append("L="+df.format(l));
		builder.append(seperator);
		builder.append("L"+subscript("q")+"="+df.format(lq));
		builder.append(seperator);
		builder.append("W="+df.format(w));
		builder.append(seperator);
		builder.append("W"+subscript("q")+"="+df.format(wq));
		
		return builder.toString();
	}
	public String solveMM1KModel(double mu, double lambda, int k) {
		
		StringBuilder builder=new StringBuilder();
		double Rho=lambda/mu;//Rho
		double l=0.0;
		double lq=0.0;
		double pk=0.0;
		double w=0.0;
		double wq=0.0;
		
		if(Rho==1.0) {
			l=k/2.0;
			pk=1.0/(k+1);
			
		}else {
			l=Rho*((1-(k+1)*pow(Rho, k)+k*pow(Rho, k+1))/((1-Rho)*(1-pow(Rho, k+1))));
			pk=pow(Rho, k)*((1-Rho)/(1-pow(Rho, k+1)));
			
		}
		lq=l-Rho*(1-pk);
		w=l/(lambda*(1-pk));
		wq=w-1/mu;
		builder.append("L="+df.format(l));
		builder.append(seperator);
		builder.append("P"+subscript(k+"")+"="+df.format(pk));
		builder.append(seperator);
		builder.append("L"+subscript("q")+"="+df.format(lq));
		builder.append(seperator);
		builder.append("W="+df.format(w));
		builder.append(seperator);
		builder.append("W"+subscript("q")+"="+df.format(wq));
		return builder.toString();
	}
	public String solveMMCModel(double mu, double lambda, int c) throws Exception {
	//	if(c==1&&mu<=lambda)throw new Exception();
		StringBuilder builder=new StringBuilder();
		double p0=calculateP0(mu,lambda,c);
		double lq=(pow((lambda/mu),c)*lambda*mu/(factorail(c-1)*pow(c*mu-lambda,2)))*p0;
		double l=lq+lambda/mu;
		double w=lq/lambda+1/mu;
		double wq=lq/lambda;
		builder.append("L="+df.format(l));
		builder.append(seperator);
		builder.append("P"+subscript(0+"")+"="+df.format(p0));
		builder.append(seperator);
		builder.append("L"+subscript("q")+"="+df.format(lq));
		builder.append(seperator);
		builder.append("W="+df.format(w));
		builder.append(seperator);
		builder.append("W"+subscript("q")+"="+df.format(wq));
		return builder.toString();
	
	}
	private double calculateP0(double mu, double lambda, int c) {
		// TODO Auto-generated method stub
		double sigma=calculateSigma(0,c,mu,lambda);
		double f=1/factorail(c)*pow((lambda/mu),c)*((c*mu)/(c*mu-lambda));
		//System.out.println(1/factorail(c));
		return 1/(sigma+f);
	}

	private double calculateSigma(int n, int j, double mu, double lambda) {
		double sum=0;
		for(;n<j;n++) {
			double factorail=1/factorail(n);
			double pow=pow((lambda/mu),n);
			sum+=factorail*pow;
		}
		//System.out.println(sum);
		return sum;
	}

	private double factorail(int i) {
		if(i==0||i==1)return 1.0;
		return i*factorail(i-1);
	}

	private String subscript(String ch) {
		return String.format("<sub>%s</sub>", ch);
	}

	public String solveMMCKModel(double mu, double lambda, int c, int k) {
		StringBuilder builder=new StringBuilder();
		double r=lambda/mu;
		double Rho=r/c;//Rho
		double p0=calculateP0MMCK(mu,lambda,c,k);
		double lq=(Rho*p0*pow(r,c))/(factorail(c)*pow(1-Rho,2))*(1-pow(Rho,k-c+1)-(1-Rho)*(k-c+1)*pow(Rho,k-c));
		double l=lq+c-p0*sigmaMMCK(0,c,r);
		double pk=pow(r,k)*p0/(pow(c,k-c)*factorail(c));
		double w=l/(lambda*(1-pk));
		double wq=lq/(lambda*(1-pk));
		builder.append("L="+df.format(l));
		builder.append(seperator);
		builder.append("P"+subscript(0+"")+"="+df.format(p0));
		builder.append(seperator);
		builder.append("P"+subscript(k+"")+"="+df.format(pk));
		builder.append(seperator);
		builder.append("L"+subscript("q")+"="+df.format(lq));
		builder.append(seperator);
		builder.append("W="+df.format(w));
		builder.append(seperator);
		builder.append("W"+subscript("q")+"="+df.format(wq));
		return builder.toString();

	}

	private double sigmaMMCK(int n, int c, double r) {
		// TODO Auto-generated method stub
		double sum=0;
		for(;n<c;n++) {
			sum+=(c-n)*pow(r,n)/factorail(n);
			
		}
		return sum;
	}

	private double calculateP0MMCK(double mu, double lambda, int c, int k) {
		double r=lambda/mu;
		double Rho=r/c;
		double p0=0;
		double sigma=calculateSigma(0, c, mu, lambda);
		if(Rho==1.0) {
			p0=sigma+(pow(r,c)/factorail(c))*(k-c+1);
			
		}else {
			p0=sigma+(pow(r,c)/factorail(c))*((1-pow(Rho,k-c+1))/(1-Rho));
		}
		return 1/p0;
	}

	


}
