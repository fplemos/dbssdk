package br.com.dbsoft.util;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.log4j.Logger;

public class DBSNumber {
	protected static Logger		wLogger = Logger.getLogger(DBSNumber.class);
	
	public static Locale 	LOCALE_PTBR = new Locale("pt", "BR");
	public static Double 	PICircle = Math.PI * 2;
	public static Double 	PICircleFactor = PICircle / 100;
	
	//------------------------------------------------------------------------------------
	/**
	 * Subtração.<br/>
	 * ex: substract(1,3,5,2) = 1 - 3 - 5 - 2<br/>
	 * Value <i>null</i> é considerado como <b>zero</b>.
	 * @param pX Sequencia de valores
	 * @return
	 */
	public static BigDecimal subtract(Object... pX){
		BigDecimal xX = BigDecimal.ZERO;
		for (int i=0; i < pX.length; i++){
			if (i==0){
				xX = toBigDecimal(pX[i]);
			}else{
				xX = xX.subtract(toBigDecimal(pX[i]));
			}
		}
		return toBigDecimal(xX);
	}
	
	//------------------------------------------------------------------------------------

	/**
	 * Adição.<br/>
	 * ex: add(1,3,5,2) = 1 + 3 + 5 + 2<br/>
	 * Value <i>null</i> é considerado como <b>zero</b>.
	 * @param pX Sequencia de valores
	 * @return Resultado
	 */
	public static BigDecimal add(Object... pX){
		BigDecimal xX = BigDecimal.ZERO;
		for (int i=0; i < pX.length; i++){
			if (i==0){
				xX = toBigDecimal(pX[i]);
			}else{
				xX = xX.add(toBigDecimal(pX[i]));
			}
		}
		return toBigDecimal(xX);
	}
	
	//------------------------------------------------------------------------------------
	/**
	 * Multiplicação.<br/>
	 * ex: multiply(1,3,5,2) = ((1 * 3) * 5) * 2<br/>
	 * Value <i>null</i> é considerado como <b>zero</b>.
	 * @param pX Sequencia de valores
	 * @return Resultado
	 */
	public static BigDecimal multiply(Object... pX){
		BigDecimal xX = BigDecimal.ZERO;
		for (int i=0; i < pX.length; i++){
			if (i==0){
				xX = toBigDecimal(pX[i]);
			}else{
				xX = xX.multiply(toBigDecimal(pX[i]));
			}
		}
		return toBigDecimal(xX);
	}	
	
	//------------------------------------------------------------------------------------

	/**
	 * Divisão, com a quantidade de casas decimals limitada a 30 caracteres.<br/>
	 * ex: divide(1,3,5,2) = ((1 / 3) / 5) / 2<br/>
	 * Divisão por <b>zero</b> ou por <i>null</i> retornam <i>null</i>.
	 * @param pCasas Quantidade máxima de casas decimais
	 * @param pX Sequencia de valores
	 * @return Resultado
	 */
	public static BigDecimal divide(Object... pX){
		BigDecimal xX = BigDecimal.ZERO;
		for (int i=0; i < pX.length; i++){
			if (i==0){
				xX = toBigDecimal(pX[i]);
			}else{
				BigDecimal xD = toBigDecimal(pX[i]);
				//Teste para evitar exception di division by zero
				if (xD.compareTo(BigDecimal.ZERO) != 0){
					xX = xX.divide(xD, 30, RoundingMode.HALF_UP);
				}else{
//					wLogger.error("Divisão por zero:" + xX.toString());
					return null;
				}
			}
		}
		return toBigDecimal(xX);
	}

	//------------------------------------------------------------------------------------

	/**
	 * Exponenciação (pX ^ pY).<br/>
	 * Números acima de 16 digitos poderão ter os digitos mais a direita ignorados.<br/>
	 * Valores nulos zerão considerados como <b>zero</b>.
	 * @param pBase Base
	 * @param pExpoente Expoente
	 * @return Resultado
	 */
	public static BigDecimal exp(Object pBase, Object pExpoente){
		Double xX = toDouble(pBase);
		Double xY = toDouble(pExpoente);
		return toBigDecimal(Math.pow(xX, xY));
	}
	
	//------------------------------------------------------------------------------------

	/**
	 * Exponenciação com base e (2.71828182845904)
	 * Números acima de 16 digitos poderão ter os digitos mais a direita ignorados.<br/>
	 * Valores nulos zerão considerados como <b>zero</b>.
	 * @param pBase Base
	 * @param pExpoente Expoente
	 * @return Resultado
	 */
	public static BigDecimal exp(Object pExpoente){
		Double xBase = 2.71828182845904D;
		return exp(xBase, pExpoente);
	}

	//------------------------------------------------------------------------------------
	
	/**
	 * Logaritmo Natural
	 * @param pX Valor que se deseja retornar o Log
	 * @return Restorna log do pX
	 */
	public static BigDecimal log(Object pX){
		if (DBSObject.isEmpty(pX)){
			return null;
		}
		Double xX = toDouble(pX);
		return toBigDecimal(Math.log(xX));
	}
	
	/**
	 * Logaritmo na Base 10
	 * @param pX Valor que se deseja retornar o Log
	 * @return Restorna log do pX
	 */
	public static BigDecimal log10(Object pX){
		if (DBSObject.isEmpty(pX)){
			return null;
		}
		Double xX = toDouble(pX);
		return toBigDecimal(Math.log10(xX));
	}
	
	//------------------------------------------------------------------------------------
	
	/**
	 * Rais Quadrada
	 * @param pX Valor que se deseja calcular a Raiz Quadrada
	 * @return Retorno da Raiz quadrada de pX
	 */
	public static BigDecimal sqrt(Object pX) {
		return toBigDecimal(Math.sqrt(toDouble(pX)));
	}
	
	//------------------------------------------------------------------------------------
	
	/**
	 * Média de uma Amostra
	 * @param pAmostra
	 * @return Retorna a média da amostra
	 */
	public static BigDecimal average(List<Double> pAmostra) {
		Double xMedia = 0D;
		
		if (DBSObject.isNull(pAmostra) || pAmostra.isEmpty()) {
			return toBigDecimal(xMedia);
		}
		
		//Calcula a Média da amostra
		for (Double xValor : pAmostra) {
			xMedia = add(xMedia,xValor).doubleValue();
		}
		xMedia = divide(xMedia, pAmostra.size()).doubleValue(); 
		
		return toBigDecimal(xMedia);
	}
	
	//------------------------------------------------------------------------------------
	
	/**
	 * Retorna coordenada a partir do ráio e percentual do arco. 100 = 360.
	 * @param pRadius
	 * @param p2PIPercentual Percentual o valor ((Math.PI * 2) / 100);
	 * @return
	 */
	
	public static Point2D circlePoint(Point2D pCenter, Double pRadius, Double p2PIPercentual){
		Point2D xX = new Point2D.Double();
		xX.setLocation(DBSNumber.round(pCenter.getX() + (pRadius * Math.sin(p2PIPercentual)), 2), 
				   	   DBSNumber.round(pCenter.getY() - (pRadius * Math.cos(p2PIPercentual)), 2));
		return xX;
	}

	/**
	 * Retorna centro a partir da altura e largura
	 * @param pWidth
	 * @param pHeight
	 * @return
	 */
	public static Point2D centerPoint(Double pWidth, Double pHeight){
		Point2D xCentro = new Point2D.Double();
		xCentro.setLocation((pWidth / 2D),
			    			(pHeight / 2D));
		return xCentro;
	}

	/**
	 * Desvio Padrão de uma Amostra
	 * Equivale a fórmula STDEV.S do Excel
	 * @param pAmostra
	 * @return Retorna o desvio padrão da amostra
	 */
	public static BigDecimal desvioPadrao(List<Double> pAmostra) {
		Double 				xDesvioPadrao = 0D;
		double[] 			xArray = new double[pAmostra.size()];
		StandardDeviation 	xSD = new StandardDeviation();
		if (DBSObject.isNull(pAmostra) || pAmostra.isEmpty()) {
			return toBigDecimal(xDesvioPadrao);
		}
		for (int xI = 0; xI < pAmostra.size(); xI++) {
			xArray[xI] = pAmostra.get(xI);
		}
		xDesvioPadrao = xSD.evaluate(xArray);
		
		return toBigDecimal(xDesvioPadrao);
	}
	
	//------------------------------------------------------------------------------------

	public static BigDecimal distribuicaoNormalInvertida(Double pNivelConfianca) {
		Double xResultado = 0D;

		NormalDistribution xDistribution = new NormalDistribution();
		xResultado = xDistribution.inverseCumulativeProbability(pNivelConfianca);
		
		return toBigDecimal(xResultado);
	}
	
	public static BigDecimal distribuicaoNormalInvertida(Double pNivelConfianca, Double pMedia, Double pDesvioPadrao) {
		Double xResultado = 0D;

		NormalDistribution xDistribution = new NormalDistribution(pMedia, pDesvioPadrao);
		xResultado = xDistribution.inverseCumulativeProbability(pNivelConfianca);
		
		return toBigDecimal(xResultado);
	}
	
	//------------------------------------------------------------------------------------
	
	public static BigDecimal distribuicaoNormal(Double pValor, Double pMedia, Double pDesvioPadrao, boolean pCumulativo) {
		Double xResultado = 0D;

		NormalDistribution xDistribution = new NormalDistribution(pMedia, pDesvioPadrao);
		if (pCumulativo) {
			xResultado = xDistribution.cumulativeProbability(pValor);
		} else {
			xResultado = xDistribution.density(pValor);
		}
		return toBigDecimal(xResultado);
	}
	
	//------------------------------------------------------------------------------------
	
	public static BigDecimal covariancia(List<Double> pAmostra1, List<Double> pAmostra2) {
		return covariancia(pAmostra1, pAmostra2, false);
	}
	public static BigDecimal covariancia(List<Double> pAmostra1, List<Double> pAmostra2, boolean pImparcial) {
		Double 		xResultado = 0D;
		double[] 	xArray1 = new double[pAmostra1.size()];
		double[] 	xArray2 = new double[pAmostra2.size()];
		Covariance 	xCovariance = new Covariance();
		
		for (int xI = 0; xI < pAmostra1.size(); xI++) {
			xArray1[xI] = pAmostra1.get(xI);
		}
		for (int xI = 0; xI < pAmostra2.size(); xI++) {
			xArray2[xI] = pAmostra2.get(xI);
		}		
		
		xResultado = xCovariance.covariance(xArray1, xArray2, pImparcial);
		return toBigDecimal(xResultado);
	}
	
	//------------------------------------------------------------------------------------
	
	public static BigDecimal variancia(List<Double> pAmostra) {
		Double 		xResultado = 0D;
		double[] 	xArray = new double[pAmostra.size()];
		Variance	xVariancia = new Variance();
		
		for (int xI = 0; xI < pAmostra.size(); xI++) {
			xArray[xI] = pAmostra.get(xI);
		}
		xResultado = xVariancia.evaluate(xArray);
		return toBigDecimal(xResultado);
	}
	
	//------------------------------------------------------------------------------------
	
	/**
	 * Retorna o k-ésimo percentil de valores em um intervalo, onde k está no intervalo 0..1, exclusivo.
	 * Utiliza o método de estimativa Percentile.EstimationType.R_7
	 * @param pAmostra A matriz ou intervalo de dados que define a posição relativa.
	 * @param pQuantile O valor do percentil no intervalo 0..1, exclusivo.
	 * @return
	 */
	public static BigDecimal percentil(List<Double> pAmostra, Double pQuantile) {
		Double 		xResultado = 0D;
		double[] 	xAmostra = new double[pAmostra.size()];
		Percentile 	xPercentil = new Percentile().withEstimationType(Percentile.EstimationType.R_7);
		
		for (int xI = 0; xI < pAmostra.size(); xI++) {
			xAmostra[xI] = pAmostra.get(xI);
		}
		xResultado = xPercentil.evaluate(xAmostra, pQuantile);
		return toBigDecimal(xResultado);
	}
	
	//------------------------------------------------------------------------------------
	
	/**
     * Emulates Excel/Calc's PMT(interest_rate, number_payments, PV, FV, Type)
     * function, which calculates the mortgage or annuity payment / yield per
     * period.
     * 
     * @param pI - periodic interest rate represented as a decimal.
     * @param pNPer - number of total payments / periods.
     * @param pPV - present value -- borrowed or invested principal.
     * @param pFV - future value of loan or annuity.
     * @param pType - when payment is made: beginning of period is 1; end, 0.
     * @return <code>double</code> representing periodic payment amount.
     */
    public static BigDecimal pmt(double pI, int pNPer, double pPV, double pFV, int pType) {
        // pmt = i / ((1 + i)^N - 1) * -(pv * (1 + i)^N + fv)
    	double xPmt = pI / (Math.pow(1 + pI, pNPer) - 1) * -(pPV * Math.pow(1 + pI, pNPer) + pFV);
    
        // account for payments at beginning of period versus end.
        if (pType == 1) {
            xPmt /= (1 + pI);
    	}
    
        // return results to caller.
        return toBigDecimal(xPmt);
    }
    
    /**
     * Overloaded pmt() call omitting type, which defaults to 0.
     * 
     * @see #pmt(double, int, double, double, int)
     */
    public static BigDecimal pmt(double r, int nper, double pv, double fv) {
        return pmt(r, nper, pv, fv, 0);
    }
    
    /**
     * Overloaded pmt() call omitting fv and type, which both default to 0.
     * 
     * @see #pmt(double, int, double, double, int)
     */
    public static BigDecimal pmt(double r, int nper, double pv) {
        return pmt(r, nper, pv, 0);
    }
    
    /**
     * Emulates Excel/Calc's IPMT(interest_rate, period, number_payments, PV,
     * FV, Type) function, which calculates the portion of the payment at a
     * given period that is the interest on previous balance.
     * 
     * @param r
     *            - periodic interest rate represented as a decimal.
     * @param per
     *            - period (payment number) to check value at.
     * @param nper
     *            - number of total payments / periods.
     * @param pv
     *            - present value -- borrowed or invested principal.
     * @param fv
     *            - future value of loan or annuity.
     * @param type
     *            - when payment is made: beginning of period is 1; end, 0.
     * @return <code>double</code> representing interest portion of payment.
     * 
     * @see #pmt(double, int, double, double, int)
     * @see #fv(double, int, double, double, int)
     */
    public static BigDecimal ipmt(double r, int per, int nper, double pv, double fv, int type) {
    
        // Prior period (i.e., per-1) balance times periodic interest rate.
        // i.e., ipmt = fv(r, per-1, c, pv, type) * r
        // where c = pmt(r, nper, pv, fv, type)
        double ipmt = fv(r, per - 1, pmt(r, nper, pv, fv, type).doubleValue(), pv, type).doubleValue() * r;
    
        // account for payments at beginning of period versus end.
        if (type == 1)
            ipmt /= (1 + r);
    
        // return results to caller.
        return toBigDecimal(ipmt);
    }
    
    /**
     * Emulates Excel/Calc's FV(interest_rate, number_payments, payment, PV,
     * Type) function, which calculates future value or principal at period N.
     * 
     * @param r
     *            - periodic interest rate represented as a decimal.
     * @param nper
     *            - number of total payments / periods.
     * @param c
     *            - periodic payment amount.
     * @param pv
     *            - present value -- borrowed or invested principal.
     * @param type
     *            - when payment is made: beginning of period is 1; end, 0.
     * @return <code>double</code> representing future principal value.
     */
    public static BigDecimal fv(double r, int nper, double c, double pv, int type) {
    
        // account for payments at beginning of period versus end.
        // since we are going in reverse, we multiply by 1 plus interest rate.
        if (type == 1)
            c *= (1 + r);
    
        // fv = -(((1 + r)^N - 1) / r * c + pv * (1 + r)^N);
        double fv = -((Math.pow(1 + r, nper) - 1) / r * c + pv * Math.pow(1 + r, nper));
    
        // return results to caller.
        return toBigDecimal(fv);
    }
    

    
    /**
     * Overloaded fv() call omitting type, which defaults to 0.
     * 
     * @see #fv(double, int, double, double, int)
     */
    public static BigDecimal fv(double r, int nper, double c, double pv) {
        return fv(r, nper, c, pv);
    }
    
    
    /**
     * Emulates Excel/Calc's PPMT(interest_rate, period, number_payments, PV,
     * FV, Type) function, which calculates the portion of the payment at a
     * given period that will apply to principal.
     * 
     * @param r
     *            - periodic interest rate represented as a decimal.
     * @param per
     *            - period (payment number) to check value at.
     * @param nper
     *            - number of total payments / periods.
     * @param pv
     *            - present value -- borrowed or invested principal.
     * @param fv
     *            - future value of loan or annuity.
     * @param type
     *            - when payment is made: beginning of period is 1; end, 0.
     * @return <code>double</code> representing principal portion of payment.
     * 
     * @see #pmt(double, int, double, double, int)
     * @see #ipmt(double, int, int, double, double, int)
     */
    public static BigDecimal ppmt(double r, int per, int nper, double pv, double fv, int type) {
    
        // Calculated payment per period minus interest portion of that period.
        // i.e., ppmt = c - i
        // where c = pmt(r, nper, pv, fv, type)
        // and i = ipmt(r, per, nper, pv, fv, type)
        return subtract(pmt(r, nper, pv, fv, type), ipmt(r, per, nper, pv, fv, type));
    }

	/**
	 * Método de cálculo de TIR onde o Value Presente Líquido (VPL)
	 * por padrão é zero.
	 * 
	 * @param pFluxo
	 * @param pPeriodo periodo em dias para cada fluxo
	 * @return double
	 */
	public static BigDecimal TIR(double[] pFluxo, int[] pPeriodo) {
		return TIR(pFluxo, pPeriodo, 0.00001);
	}
	
	/**
	 * Método retirado da biblioteca org.apache.poi.ss.formula.functions.Irr
	 * Este método foi modificado para retornar 0 caso o numero de iterações
	 * exceda o número do fluxos.
	 * 
	 * @param pFluxo
	 * @param pVPL
	 * @return
	 */
	public static BigDecimal TIR(double[] pFluxo, int[] pPeriodo, double pVPL) {
		int 	xIteracoesMaxima = 20; //Numero de tentativas de cálculo
        double 	xPrecisao = 1E-7;
        double	x0 = pVPL;
        double 	x1;

        int xCount = 0;
        while (xCount < xIteracoesMaxima) { //pFluxo.length

        	//O valor da função (VPL) e o seu derivado são calculados no mesmo loop
            double xValue = 0;
            double fDerivado = 0;
            for (int xIndice = 0; xIndice < pFluxo.length; xIndice++) {
            	if (DBSObject.isEmpty(pPeriodo)) {
            		xValue += pFluxo[xIndice] / Math.pow(1.0 + x0, xIndice);
            		fDerivado += -xIndice * pFluxo[xIndice] / Math.pow(1.0 + x0, xIndice + 1);
            	} else {
            		xValue += pFluxo[xIndice] / Math.pow(1.0 + x0, pPeriodo[xIndice]);
            		fDerivado += -(pPeriodo[xIndice] * pFluxo[xIndice]) / Math.pow(1.0 + x0, pPeriodo[xIndice] + 1);
            	}
            }

            //Método Newton-Raphson
            x1 = x0 - xValue/fDerivado;

            if (Math.abs(x1 - x0) <= xPrecisao) {
                return toBigDecimal(x1);
            }

            x0 = x1;
            ++xCount;
        }
        //Se o numero de iterações for maio que o tamanho do fluxo. 
        return BigDecimal.ZERO;
    }

	/**
	 * Calcula preço médio utilizando média ponderada
	 * @param pPuAtual 				Preço atual
	 * @param pQuantidadeAtual 		Quantidade atual
	 * @param pPuOperado 			Preço da operação
	 * @param pQuantidadeOperada	Quantidade da operação
	 * @param pExclusao				Indica que está sendo uma operação de exclusão
	 * @return						Pu médio calculado
	 */
	public static BigDecimal calculaPuMedio(Object pPuAtual, Object pQuantidadeAtual, Object pPuOperado, Object pQuantidadeOperada, boolean pExclusao){
		if (pPuAtual == null 
		 || pQuantidadeAtual == null 
		 || pPuOperado == null 
		 ||	pQuantidadeOperada == null){
			return null;
		}
		
		Double 		xSaldo;
		Double 		xFinOperado;
		Double 		xFinAtual;
		
		BigDecimal 	xPuAtual = toBigDecimal(pPuAtual);
		BigDecimal	xPuOperado = toBigDecimal(pPuOperado);
		Double 		xQuantidadeAtual = toDouble(pQuantidadeAtual);
		Double 		xQuantidadeOperada = toDouble(pQuantidadeOperada);
		
		xSaldo = add(xQuantidadeAtual, xQuantidadeOperada).doubleValue();
		
		if (xSaldo.equals(0D)){
			return xPuAtual;
		} else {
			if (isRealizacao(pQuantidadeAtual, pQuantidadeOperada) == pExclusao){
				/* 
				 * Preço médio deve ser calcula quando:
	             * Ocorrer uma nova movimentação com o mesmo sinal da posição ou
	             * for excluido uma movimentação que mudou a preço médio.
	             */
				xFinOperado = multiply(pQuantidadeOperada, pPuOperado).doubleValue();
				xFinAtual = multiply(pQuantidadeAtual, pPuAtual).doubleValue();
				
				return divide(add(xFinOperado, xFinAtual), xSaldo);
			} else {
				if (isPosicaoVirada(pQuantidadeAtual, pQuantidadeOperada)){
					return xPuOperado;
				} else {
					/*
					 * Mantém o preço da posição quando:
					 *   movimentação for realização sem virar a posição ou for exclusão da mesma
					 */
					return xPuAtual;
				}
			}
		}	
	}

	/**
	 * Verifica se é uma virada de posição ou inicio de uma nova posição (partindo do estoque zerado).
     * Verifica se posição passa a ficar negativa quando estava positiva ou vice-versa.
	 * @param pQuantidadeAtual		Quantidade atual
	 * @param pQuantidadeOperada	Quantidade da operação
	 * @return TRUE para virada de posição e FALSE caso contrário 
	 */
	public static boolean isPosicaoVirada(Object pQuantidadeAtual, Object pQuantidadeOperada){
		if (pQuantidadeAtual == null || 
			pQuantidadeOperada == null){
			return false;
		}
		
		Double xSaldo;
		Double xQuantidadeAtual = toDouble(pQuantidadeAtual);
		Double xQuantidadeOperada = toDouble(pQuantidadeOperada);
		xSaldo = add(xQuantidadeAtual, xQuantidadeOperada).doubleValue();
		
		/*
		 * Posição é considerada virada quando:
		 * xSaldo(Novo Saldo) diferente de zero e
		 * Sinal de xSaldo deferente do estoque atual(pQuantidadeAtual) ou estoque atual(pQuantidadeAtual) = 0  
	    */
		
		if (!xSaldo.equals(0D) 
		  && (sign(xSaldo).intValue() != sign(xQuantidadeAtual).intValue() 
		   || xQuantidadeAtual.equals(0D))){
			return true;
		}
		return false;
	}

	/**
	 * Verifica se a operação é uma realização da posição.
	 * @param pQuantidadeAtual		Quantidade Atual
	 * @param pQuantidadeOperada	Quantidade que está sendo operada
	 * @return TRUE é Operação de realização, FALSE não é operação de realização
	 */
	public static boolean isRealizacao(Object pQuantidadeAtual, Object pQuantidadeOperada){
		if (pQuantidadeAtual == null 
		 || pQuantidadeOperada == null){
			return false;
		}
		Double xQuantidadeAtual = toDouble(pQuantidadeAtual);
		Double xQuantidadeOperada= toDouble(pQuantidadeOperada);		
		
		if ((xQuantidadeAtual < 0 && xQuantidadeOperada > 0) 
		 || (xQuantidadeAtual > 0 && xQuantidadeOperada < 0)){
			return true;
		}
		return false;
	}
	/**
	 * Sinal do valor
	 * @param pValue Valor que se deseja conhecer o sinal
	 * @return Retorna 0 = Zero / 1 = Positivo / -1 = Negativo
	 */
	public static Integer sign(Object pValue){
		return toBigDecimal(pValue).signum();
	}

	/**
	 * Retorna um número randomico 0 > 1.
	 * @return Retorna número randômico
	 */
	public static BigDecimal random(){
		return toBigDecimal(Math.random());
	}
	//---------------------------------------------------------------------------------

	/**
	 * Retornar o valor sem sinal
	 * @param pValue Valor que se deseja retinar o sinal
	 * @return Restorna valor sem sinal
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static Double abs(Double pValue) {
		return pvAbs(pValue, Double.class);
	}
	
	/**
	 * Retornar o valor sem sinal
	 * @param pValue Valor que se deseja retinar o sinal
	 * @return Restorna valor sem sinal
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static Long abs(Long pValue) {
		return pvAbs(pValue, Long.class);
	}

	/**
	 * Retornar o valor sem sinal
	 * @param pValue Valor que se deseja retinar o sinal
	 * @return Restorna valor sem sinal
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static Integer abs(Integer pValue) {
		return pvAbs(pValue, Integer.class);
	}
	
	/**
	 * Retornar o valor sem sinal
	 * @param pValue Valor que se deseja retinar o sinal
	 * @return Restorna valor sem sinal
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static BigDecimal abs(Object pValue) {
		return pvAbs(pValue, Object.class);
	}
	//----------------------------------------------------------------------------------
	/**
	 * Retorna valor truncado.<br/>
	 * Caso a quantidade de casas seja negativa irá truncar também parte inteira. 
	 * @param pValue Valor que se deseja truncar
	 * @param pDecimalPlaces Quantidade de casas decimais
	 * @return Resultado
	 */
	public static Double trunc(Double pValue, Integer pDecimalPlaces) {
		return pvTrunc(pValue, pDecimalPlaces, Double.class);
	}
	
	/**
	 * Retorna valor truncado.<br/>
	 * Caso a quantidade de casas seja negativa irá truncar também a parte inteira. 
	 * @param pValue Valor que se deseja truncar
	 * @param pDecimalPlaces Quantidade de casas decimais
	 * @return Resultado
	 */
	public static Long trunc(Long pValue, Integer pDecimalPlaces) {
		return pvTrunc(pValue, pDecimalPlaces, Long.class);
	}

	/**
	 * Retorna valor truncado.<br/>
	 * Caso a quantidade de casas seja negativa irá truncar também a parte inteira. 
	 * @param pValue Valor que se deseja truncar
	 * @param pDecimalPlaces Quantidade de casas decimais
	 * @return Resultado
	 */
	public static Integer trunc(Integer pValue, Integer pDecimalPlaces) {
		return pvTrunc(pValue, pDecimalPlaces, Integer.class);
	}
	
	/**
	 * Retorna valor truncado.<br/>
	 * Caso a quantidade de casas seja negativa irá truncar também a parte inteira. 
	 * @param pValue Valor que se deseja truncar
	 * @param pDecimalPlaces Quantidade de casas decimais
	 * @return Resultado
	 */
	public static BigDecimal trunc(Object pValue, Integer pDecimalPlaces) {
		return pvTrunc(pValue, pDecimalPlaces, Object.class);
	}

	//----------------------------------------------------------------------------------
	/**
	 * Retorna valor arredondado<br/>
	 * Exemplo: arrendodamento com 1 casa decimal: 1,45 = 1,5  | 1,44 = 1,4).<br/>
	 * Caso a quantidade de casas seja negativa irá arrendodar também a parte inteira. 
	 * @param pValue Valor que se deseja arredondar
	 * @param pDecimalPlaces Quantidade de casas decimais
	 * @return Resultado
	 */
	public static Double round(Double pValue, Integer pDecimalPlaces) {
		return pvRound(pValue, pDecimalPlaces, Double.class);
	}
	
	/**
	 * Retorna valor arredondado<br/>
	 * Exemplo: arrendodamento com 1 casa decimal: 1,45 = 1,5  | 1,44 = 1,4).<br/>
	 * Caso a quantidade de casas seja negativa irá arrendodar também a parte inteira. 
	 * @param pValue Valor que se deseja arredondar
	 * @param pDecimalPlaces Quantidade de casas decimais
	 * @return Resultado
	 */
	public static Long round(Long pValue, Integer pDecimalPlaces) {
		return pvRound(pValue, pDecimalPlaces, Long.class);
	}

	/**
	 * Retorna valor arredondado<br/>
	 * Exemplo: arrendodamento com 1 casa decimal: 1,45 = 1,5  | 1,44 = 1,4).<br/>
	 * Caso a quantidade de casas seja negativa irá arrendodar também a parte inteira. 
	 * @param pValue Valor que se deseja arredondar
	 * @param pDecimalPlaces Quantidade de casas decimais
	 * @return Resultado
	 */
	public static Integer round(Integer pValue, Integer pDecimalPlaces) {
		return pvRound(pValue, pDecimalPlaces, Integer.class);
	}
	
	/**
	 * Retorna valor arredondado<br/>
	 * Exemplo: arrendodamento com 1 casa decimal: 1,45 = 1,5  | 1,44 = 1,4).<br/>
	 * Caso a quantidade de casas seja negativa irá arrendodar também a parte inteira. 
	 * @param pValue Valor que se deseja arredondar
	 * @param pDecimalPlaces Quantidade de casas decimais
	 * @return Resultado
	 */
	public static BigDecimal round(Object pValue, Integer pDecimalPlaces) {
		return pvRound(pValue, pDecimalPlaces, Object.class);
	}


	//-------------------------------------------------------------------
	/**
	 * Retorna valor inteiro 
	 * @param pValue Valor que se deseja a parte inteira
	 * @return Retorna valor inteiro
	 */
	public static Double inte(Double pValue){
		return trunc(pValue, 0);
	}
	
	/**
	 * Retorna valor inteiro 
	 * @param pValue Valor que se deseja a parte inteira
	 * @return Retorna valor inteiro
	 */
	public static Long inte(Long pValue){
		return trunc(pValue, 0);
	}
	
	/**
	 * Retorna valor inteiro 
	 * @param pValue Valor que se deseja a parte inteira
	 * @return Retorna valor inteiro
	 */
	public static Integer inte(Integer pValue){
		return trunc(pValue, 0);
	}
	
	/**
	 * Retorna valor inteiro 
	 * @param pValue Valor que se deseja a parte inteira
	 * @return Retorna valor inteiro
	 */
	public static BigDecimal inte(Object pValue){
		return trunc(pValue, 0);
	}

	//---------------------------------------------------------------------------------

	/**
	 * Retorna valor negativo ou positivo conforme paremeto <b>pPositive<b/> independetemente do sinal do valor recebido.
	 * @param pValue
	 * @param pSign 1 = Positivo / -1 = Negativo
	 * @return
	 */
	public static Double toPositive(Double pValue, Boolean pPositive){
		return pvToPositive(pValue, pPositive, Double.class);
	}
	
	/**
	 * Retorna valor negativo ou positivo conforme paremeto <b>pPositive<b/> independetemente do sinal do valor recebido.
	 * @param pValue
	 * @param pSign 1 = Positivo / -1 = Negativo
	 * @return
	 */
	public static Long toPositive(Long pValue, Boolean pPositive){
		return pvToPositive(pValue, pPositive, Long.class);
	}

	/**
	 * Retorna valor negativo ou positivo conforme paremeto <b>pPositive<b/> independetemente do sinal do valor recebido.
	 * @param pValue
	 * @param pSign 1 = Positivo / -1 = Negativo
	 * @return
	 */
	public static Integer toPositive(Integer pValue, Boolean pPositive){
		return pvToPositive(pValue, pPositive, Integer.class);
	}
	
	/**
	 * Retorna valor negativo ou positivo conforme paremeto <b>pPositive<b/> independetemente do sinal do valor recebido.
	 * @param pValue
	 * @param pSign 1 = Positivo / -1 = Negativo
	 * @return
	 */
	public static BigDecimal toPositive(Object pValue, Boolean pPositive){
		return pvToPositive(pValue, pPositive, Object.class);
	}

	//-----------------------------------------------------------------------
	/**
	 * Retorna se a string é um valor numérico
	 * @param pTextoNumerico Texto se será verificado 
	 * @return true = é um valor numérico / false = NÃO é um valor numérico
	 */
	public static boolean isNumber(String pTextoNumerico){
		if (pTextoNumerico == null){
			return false;
		}
		return pTextoNumerico.matches("([-+]?\\d*\\.*\\,*\\d+)([eE][+-]?[0-9]+)?$");
	}
	
	/**
	 * Retorna se número é inteiro(Não contém casas decimais)
	 * @param pValue
	 * @return
	 */
	public static boolean isInteger(Object pValue){
		Integer xInteiro = toInteger(pValue);
		if (xInteiro != null){
			if (xInteiro.toString().trim().equals(pValue.toString().trim())){
				return true;
			}
		}
		return false;
	}

	/** 
	 * Retorna string contendo somente os valores numérico que existem na string informada.
	 * Antigo: getOnlyNumber
	 * @param pTexto
	 * @return String somente com valores numéricos. Caso não exista valores numéricos, retorna 0(zero)
	 */
	public static String getOnlyNumber(String pTexto){
		if (DBSObject.isEmpty(pTexto)){
			return null;
		}
		String xA = "";
		String xB = "";
		for (int x=0;x<pTexto.length();x++){
			xB = pTexto.substring(x, x+1);
			if (isNumber(xB)){
				xA += xB;
			}
		}
		return DBSString.getNotEmpty(xA, "0");
	}	
	
	/**
	 * Retorna um número a partir de uma string, forçando uma determinada quantidade de casas decimais.<br/>
	 * Utilizado para converter string contendo números sem o sinal da casa decimal<br/>
	 * VB:Antigo StringToNumber
	 * @param pString String com conteúdo a ser utilizado
	 * @param pDecimalPlaces Quantidade de Casas decimais a serem consideradas.
	 * @return Valor numérico
	 */
	public static BigDecimal toNumber(String pString, Integer pDecimalPlaces){
		if (DBSObject.isEmpty(pString) ||
			DBSObject.isEmpty(pDecimalPlaces)){
			return null;
		}
		BigDecimal xD;
		BigDecimal xE = exp(10D, pDecimalPlaces.doubleValue());
	
		if (isNumber(pString)){
			xD = toBigDecimal(pString);
			xD = divide(xD, xE);
			xD = round(xD, pDecimalPlaces);
			return xD;
		}
		else{
			return null;
		}
	}

	/**
	 * Converte para BigDecimal.<br/>
	 * Utiliza a localidade para identificar qual o sinal da casa decimal.<br/>
	 * Valor será limitado a 90 digitos significativos a esquerda para a direita.<br/>
	 * @return Retorna o valor convertido para BigDecimal ou 0(zero) caso o valor a ser convertido seja nulo
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object pValue) {
		return toBigDecimal(pValue, 0);
	}

	/**
	 * Converte para BigDecimal.<br/>
	 * Utiliza a localidade para identificar qual o sinal da casa decimal.<br/>
	 * Valor será limitado a 90 digitos significativos a esquerda para a direita.<br/>
	 * @param pDefaultValue
	 * @return Retorna o valor convertido para BigDecimal ou o valor informado em pDefaultValue caso o valor a ser convertido seja nulo
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object pValue, Object pDefaultValue) {
		try{
			if (DBSObject.isEmpty(pValue)){
				if (pDefaultValue == null){
					return null;
				}else{
					//Obriga que defaultValue passe pela rotina para criar um BigDecimal padrão.
					pValue = pDefaultValue;
				}
			}
			BigDecimal xValue;
			if (pValue instanceof Number) {
				//Usa contrutor passando string para evitar conversão errada que ocorre quando o pValue for Double
				xValue = new BigDecimal(DBSString.getSubString(pValue.toString(), 1, 90), MathContext.UNLIMITED);
				//Força que valor retorne somente um "0" caso o valor seja zero porém esteja como "0.0"
				if (xValue.compareTo(BigDecimal.ZERO) == 0){
					xValue = BigDecimal.ZERO;
				}
			} else if (pValue instanceof String) {	
				Number xN = pvStringToNumberFormat((String) pValue, LOCALE_PTBR);
				if (xN != null){
					xValue = new BigDecimal(DBSString.getSubString(xN.toString(), 1, 90), MathContext.UNLIMITED);
				}else{
					xValue = null;
				}
			} else {
				return null; 
			}
			//Força que os zeros a esquerda seja retirados antes de retornar o valor
			return pvBigDecimalStripTrailingZeros(xValue);
		}catch(Exception e){
			wLogger.error(e);
			return null;
		}
	}
	
	/**
	 * Converte para Long.<br/>
	 * Se for nulo, retorna Zero.<br/>
	 * Utiliza a localidade para identificar qual o sinal da casa decimal
	 * caso o valor informado seja uma String.
	 * Se for nulo, retorna Zero.<br/>
	 * Utiliza a localidade para identificar qual o sinal da casa decimal
	 * caso o valor informado seja uma String.
	 * @param pValue
	 * @return Retorna o valor convertido ou o 0(zero), caso o valor informado seja nulo.
	 */
	public static Long toLong(Object pValue) {
		return toLong(pValue, 0L);
	}

	/**
	 * Converte para Long.<br/>
	 * Utiliza a localidade para identificar qual o sinal da casa decimal
	 * caso o valor informado seja uma String.
	 * @param pValue
	 * @param pDefaultValue
	 * @return Retorna o valor convertido ou o valor informado em pDefaultValue caso o valor a ser convertido seja nulo
	 */
	public static Long toLong(Object pValue, Long pDefaultValue) {
		if (DBSObject.isEmpty(pValue)) { 
			return pDefaultValue;
		}
		
		if (pValue instanceof Integer) {
			return new Long((Integer) pValue);
		} else if (pValue instanceof BigDecimal) {
			return ((BigDecimal) pValue).longValue();
		} else if (pValue instanceof Double) {
			return ((Double) pValue).longValue();
		} else if (pValue instanceof Float) {
			return ((Float) pValue).longValue();
		} else if (pValue instanceof Long) {
			return (Long) pValue;
		} else if (pValue instanceof Boolean) {
			if (!(Boolean) pValue) {
				return 0L;
			} else {
				return -1L;
			}
		} else if (pValue instanceof String) {	
			Number xN = pvStringToNumberFormat((String) pValue, LOCALE_PTBR);
			if (xN != null){
				return xN.longValue();
			}
		}
		return null;
	}
	
	/**
	 * Converte para Integer.<br/>
	 * Se for nulo, retorna Zero.<br/>
	 * Utiliza a localidade para identificar qual o sinal da casa decimal
	 * caso o valor informado seja uma String.
	 * @param pValue 
	 * @return Retorna o valor convertido ou o 0(zero), caso o valor informado seja nulo.
	 */
	public static Integer toInteger(Object pValue) {
		return toInteger(pValue, 0);
	}
	
	/**
	 * Converte para Integer
	 * Utiliza a localidade para identificar qual o sinal da casa decimal
	 * caso o valor informado seja uma String.
	 * @param pValue
	 * @param pDefaultValue
	 * @return Retorna o valor convertido ou o valor informado em pDefaultValue caso o valor a ser convertido seja nulo
	 */
	public static Integer toInteger(Object pValue, Integer pDefaultValue) {
		if (DBSObject.isEmpty(pValue)) { 
			return pDefaultValue;
		}

		if (pValue instanceof Integer) {
			return (Integer) pValue;
		} else if (pValue instanceof BigDecimal) {
			return ((BigDecimal) pValue).intValue();
		} else if (pValue instanceof Double) {
			return ((Double) pValue).intValue();
		} else if (pValue instanceof Float) {
			return ((Float) pValue).intValue();
		} else if (pValue instanceof Long) {
			return ((Long) pValue).intValue();
		} else if (pValue instanceof Boolean) {
			if (!(Boolean) pValue) {
				return 0;
			} else {
				return -1;
			}
		} else if (pValue instanceof String) {	
			Number xN = pvStringToNumberFormat((String) pValue, LOCALE_PTBR);
			if (xN != null){
				return xN.intValue();
			}
		}
		return null;
	}
	
	/**
	 * Converte para Double.<br/> 
	 * Se for nulo, retorna Zero.<br/>
	 * Utiliza a localidade para identificar qual o sinal da casa decimal
	 * caso o valor informado seja uma String.
	 * @param pValue
	 * @return Retorna o valor convertido ou o valor 0.0, caso o valor informado seja nulo
	 */
	public static Double toDouble(Object pValue) {
		return toDouble(pValue, 0D);
	}
	
	/**
	 * Converte para Double.<br/>
	 * Utiliza a localidade para identificar qual o sinal da casa decimal
	 * caso o valor informado seja uma String.
	 * @param pValue
	 * @param pDefaultValue
	 * @return Retorna o valor convertido ou o valor informado em pDefaultValue caso o valor a ser convertido seja nulo
	 */
	public static Double toDouble(Object pValue, Double pDefaultValue) {
		return toDouble(pValue, pDefaultValue, LOCALE_PTBR);
	}
	
	public static Double toDouble(Object pValue, Double pDefaultValue, Locale pLocale) {
		if (DBSObject.isEmpty(pValue) || pValue.equals("")) {
			return pDefaultValue;
		}
		if (pValue instanceof Double) {
			return (Double) pValue;
		} else if (pValue instanceof BigDecimal) {
			return ((BigDecimal) pValue).doubleValue();
		} else if (pValue instanceof Integer) {
			return ((Integer)pValue).doubleValue();
		} else if (pValue instanceof Long) {
			return ((Long)pValue).doubleValue();
		} else if (pValue instanceof String) {	
			Number xN = pvStringToNumberFormat((String) pValue, pLocale);
			if (xN != null){
				return xN.doubleValue();
			}
		} 
		return null;
	}

	/**
	 * Retorna o mesmo valor informado quando este não for zero.<br/> 
	 * Se for zero, retorna o conteúdo default definido pelo usuário ou <b>1</>, quando este for zero.
	 * Se for zero, retorna 1.
	 * @param pDado Dado a ser verificado
	 * @return Dado contendo o valor diferente de zero
	 */
	public static Double toDoubleNotZero(Object pDado){
		return toDoubleNotZero(pDado, 1D);
	}

	/**
	 * Retorna o mesmo valor informado quando este não for zero.<br/> 
	 * Se for zero, retorna o conteúdo default definido pelo usuário ou <b>1</>, quando este for zero.
	 * @param pDado Dado a ser verificado
	 * @param pDefaultValue Conteúdo a ser considerado caso o dado seja vazio
	 * @return Dado contendo o valor diferente de zero
	 */
	public static Double toDoubleNotZero(Object pDado, Double pDefaultValue){
		if (DBSObject.isEmpty(pDefaultValue)){
			pDefaultValue = 1D; //Reseta valor default para 1 se não for informado na chamada deste função
		}
		Double xValue = toDouble(pDado, pDefaultValue);
		if(DBSObject.isEmpty(xValue) 
		|| xValue.equals(0D)){
			xValue = pDefaultValue;
		}
		return xValue;
	}

	/**
	 * Retorna o mesmo valor informado quando este não for zero.<br/> 
	 * Se for zero, retorna o conteúdo default definido pelo usuário ou <b>1</>, quando este for zero.
	 * @param pDado Dado a ser verificado
	 * @return Dado contendo o valor diferente de zero
	 */
	public static BigDecimal toBigDecimalNotZero(Object pDado){
		return toBigDecimalNotZero(pDado, BigDecimal.ONE);
	}

	/**
	 * Retorna o mesmo valor informado quando este não for zero.<br/> 
	 * Se for zero, retorna o conteúdo default definido pelo usuário ou <b>1</>, quando este for zero.
	 * @param pDado Dado a ser verificado
	 * @param pDefaultValue Conteúdo a ser considerado caso o dado seja vazio
	 * @return Dado contendo o valor diferente de zero
	 */
	public static BigDecimal toBigDecimalNotZero(Object pDado, BigDecimal pDefaultValue){
		if (DBSObject.isEmpty(pDefaultValue)){
			pDefaultValue = BigDecimal.ONE; //Reseta valor default para 1 se não for informado na chamada deste função
		}
		BigDecimal xValue = toBigDecimal(pDado, pDefaultValue);
		if(DBSObject.isEmpty(xValue) 
		|| xValue.equals(BigDecimal.ZERO)){
			xValue = pDefaultValue;
		}
		return pvBigDecimalStripTrailingZeros(xValue);
	}

	/**
	 * Retorna uma lista (do tipo Integer) a patir de uma String, separado por um delimitador informado
	 * 
	 * @param pTextoBase String com o texto que se deseja separar
	 * @param pDelimitador String que será utilizado para separar os campos. Não faz a delimitação dos campos se delimitador for nulo ou vázio
	 * @return Lista com o conteúdo em cada linha 
	 */
	public static List<Integer> toArrayList(String pTextoBase, String pDelimitador){
		List<Integer> xListaRetorno = new ArrayList<Integer>();
		ArrayList<String> xListaStrings = DBSString.toArrayList(pTextoBase, pDelimitador);
		
		for (String xValor : xListaStrings) {
			xListaRetorno.add(DBSNumber.toInteger(xValor));
		}
		
		return xListaRetorno; 
	}
	
	//===========================================================================================================
	// PRIVATE
	//===========================================================================================================

	
	/**
	 * Rertorna valor absoluto
	 * @param pValue
	 * @return
	 */
	private static <T extends Number> T pvAbs(Object pValue, Class<?> pClass){
		if (pValue == null){return null;}
		BigDecimal xValue = toBigDecimal(pValue);
	
		xValue = xValue.abs();
		return pvConvertToClass(xValue, pClass);
	}

	/**
	 * Rertorna valor truncado
	 * @param pValue
	 * @return
	 */
	private static <T extends Number> T pvTrunc(Object pValue, Integer pDecimalPlaces, Class<?> pClass){
		if (pValue == null){return null;}
		BigDecimal xValue;
	
		if (pDecimalPlaces==null){
			pDecimalPlaces = 0;
		}
		if (toInteger(pValue) > 0 ){
			xValue = toBigDecimal(pValue).setScale(pDecimalPlaces, RoundingMode.FLOOR);
		}else{
			xValue = toBigDecimal(pValue).setScale(pDecimalPlaces, RoundingMode.HALF_UP);
		}
		xValue = toBigDecimal(xValue);
		return pvConvertToClass(xValue, pClass);
	}

	/**
	 * Rertorna valor truncado
	 * @param pValue
	 * @return
	 */
	private static <T extends Number> T pvRound(Object pValue, Integer pDecimalPlaces, Class<?> pClass){
		if (pValue == null){return null;}
		BigDecimal xValue;

		if (pDecimalPlaces==null){
			pDecimalPlaces = 0;
		}
		xValue = toBigDecimal(pValue).setScale(pDecimalPlaces, RoundingMode.HALF_UP);
		xValue = toBigDecimal(xValue);
		return pvConvertToClass(xValue, pClass);
	}

	/**
	 * Retorna valor negativo ou positivo conforme paremeto <b>pPositive<b/> independetemente do sinal do valor recebido.
	 * @param pValue
	 * @param pPositive
	 * @param pClass Para para a qual o valor será convertido.
	 * @return
	 */
	private static <T extends Number> T pvToPositive(Object pValue, Boolean pPositive, Class<?> pClass){
		if (pClass==null
		 || pValue== null){
			return null;
		}
		BigDecimal xValue = toBigDecimal(pValue);
		if (pPositive != null){
			if (pPositive){
				xValue = abs(xValue);
			}else{
				xValue = multiply(abs(xValue), -1);
			}
		}
		return pvConvertToClass(xValue, pClass);
	}
	/**
	 * Converte para o valor recebido para o tipo informado
	 * @param pValue
	 * @param pClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Number> T pvConvertToClass(BigDecimal pValue, Class<?> pClass){
		BigDecimal xValue = pvBigDecimalStripTrailingZeros(pValue);
		if (DBSObject.isEmpty(pValue)){
			return null;
		}		
		if (Double.class.isAssignableFrom(pClass)){
			return (T) toDouble(xValue);
		}else if (Long.class.isAssignableFrom(pClass)){
			return (T) toLong(xValue);
		}else if (Integer.class.isAssignableFrom(pClass)){
			return (T) toInteger(xValue);
		}else{
			return (T) xValue;
		}
	}

	private static BigDecimal pvBigDecimalStripTrailingZeros(BigDecimal pValue){
		return new BigDecimal(pValue.stripTrailingZeros().toPlainString(), MathContext.UNLIMITED);
	}

	/** 
	 * Converte string para double, considerando a localidade BR(",") para o sinal decimal.<br/>  
	 * @param pValue
	 * @return
	 */
	private static Number pvStringToNumberFormat(String pValue, Locale pLocale){
		Boolean xPerc = (pValue !=null && pValue.indexOf("%") > 0); //Verifica se valor é um percentual
		DecimalFormat xNF =  (DecimalFormat) DecimalFormat.getInstance(pLocale);
		xNF.setParseBigDecimal(true);
		xNF.setMaximumFractionDigits(30);
		xNF.setRoundingMode(RoundingMode.HALF_UP);
		xNF.setDecimalSeparatorAlwaysShown(false); 
		try {
			if (pValue!=null){
				if (xPerc){
					return (xNF.parse(pValue).doubleValue() / 100D);
				}else{
					return xNF.parse(pValue);
				}
			}else{
				return null;
			}
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * Retorna string contendo os valores.
	 * Este método evita o retorno de notação científica. 
	 * @param pValue
	 * @return
	 */
	public static String toPlainString(Double pValue){
		if (pValue == null){return "";}
		return BigDecimal.valueOf(pValue).toPlainString();
	}
}


