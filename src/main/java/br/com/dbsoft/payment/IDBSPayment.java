package br.com.dbsoft.payment;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.message.DBSMessage;
import br.com.dbsoft.message.IDBSMessage;
import br.com.dbsoft.message.IDBSMessage.MESSAGE_TYPE;
import br.com.dbsoft.message.IDBSMessages;
import br.com.dbsoft.util.DBSDate.PERIODICIDADE;
import br.com.dbsoft.util.DBSObject;
import urn.ebay.apis.eBLBaseComponents.PaymentStatusCodeType;
import urn.ebay.apis.eBLBaseComponents.RecurringPaymentsProfileStatusType;

public interface IDBSPayment {

	public static enum PROFILE_STATUS {
		ACTIVE			("Active", 		0),
		CANCELED		("Canceled", 	1),
		PENDING			("Pending", 	2),
		SUSPENDED		("Suspended", 	3),
		EXPIRED			("Expired", 	4);
		
		private String 	wName;
		private int 	wCode;
		
		private PROFILE_STATUS(String pName, int pCode) {
			this.wName = pName;
			this.wCode = pCode;
		}

		public String getName() {
			return wName;
		}

		public int getCode() {
			return wCode;
		}
		
		public static PROFILE_STATUS get(Integer pCode) {
			if (DBSObject.isNull(pCode)) {
				return null;
			}
			switch (pCode) {
			case 0:
				return ACTIVE;
			case 1:
				return CANCELED;
			case 2:
				return PENDING;
			case 3:
				return SUSPENDED;
			case 4:
				return EXPIRED;
			default:
				return null;
			}
		}
		
		public static PROFILE_STATUS getFromPayPal(RecurringPaymentsProfileStatusType pPayPalStatusType) {
			if (RecurringPaymentsProfileStatusType.ACTIVEPROFILE.equals(pPayPalStatusType)) {
				return ACTIVE;
			} else if (RecurringPaymentsProfileStatusType.CANCELLEDPROFILE.equals(pPayPalStatusType)) {
				return CANCELED;
			} else if (RecurringPaymentsProfileStatusType.PENDINGPROFILE.equals(pPayPalStatusType)) {
				return PENDING;
			} else if (RecurringPaymentsProfileStatusType.SUSPENDEDPROFILE.equals(pPayPalStatusType)) {
				return SUSPENDED;
			} else if (RecurringPaymentsProfileStatusType.EXPIREDPROFILE.equals(pPayPalStatusType)) {
				return EXPIRED;
			} else {
				return null;
			}
			
		}
	}
	
	public static enum PAYMENT_STATUS {
		COMPLETE		("Complete",	0),
		PENDING			("Pending", 	1),
		FAILED			("Failed", 		2);
		
		private String 	wName;
		private int 	wCode;
		
		private PAYMENT_STATUS(String pName, int pCode) {
			this.wName = pName;
			this.wCode = pCode;
		}

		public String getName() {
			return wName;
		}

		public int getCode() {
			return wCode;
		}
		
		public static PAYMENT_STATUS get(Integer pCode) {
			if (DBSObject.isNull(pCode)) {
				return null;
			}
			switch (pCode) {
			case 0:
				return COMPLETE;
			case 1:
				return PENDING;
			case 2:
				return FAILED;
			default:
				return null;
			}
		}
		
		public static PAYMENT_STATUS getFromPayPal(PaymentStatusCodeType pPayPalStatusType) {
			if (DBSObject.isNull(pPayPalStatusType)) {
				return null;
			}
			if (pPayPalStatusType.equals(PaymentStatusCodeType.COMPLETED)) {
				return COMPLETE;
			} else if (pPayPalStatusType.equals(PaymentStatusCodeType.PENDING)) {
				return PENDING;
			} else {
				return FAILED;
			}
			
		}
	}

	// Mensagens
	IDBSMessage MsgErroDefault	= new DBSMessage(MESSAGE_TYPE.ERROR, "Error");
	IDBSMessage MsgErroSemResposta	= new DBSMessage(MESSAGE_TYPE.ERROR, "Sem resposta.");
	IDBSMessage MsgErroValidateItemEmpty	= new DBSMessage(MESSAGE_TYPE.ERROR, "Item Name não configurado.");
	IDBSMessage MsgErroValidateQuantityInvalid	= new DBSMessage(MESSAGE_TYPE.ERROR, "Quantidade não configurada.");
	IDBSMessage MsgErroValidateValueInvalid	= new DBSMessage(MESSAGE_TYPE.ERROR, "Valor não configurado.");
	IDBSMessage MsgErroValidateSucessURLEmpty = new DBSMessage(MESSAGE_TYPE.ERROR, "URL de Sucesso não configurada.");
	IDBSMessage MsgErroValidateCancelURLEmpty = new DBSMessage(MESSAGE_TYPE.ERROR, "URL de Cancelamento não configurada.");
	
	@SuppressWarnings("rawtypes")
	public IDBSMessages getMessages();
	
	public boolean isOk();
	public void setOk(boolean pOk);
	
	/**
	 * Nome do Item.
	 * @return
	 */
	public String getItemName();
	public void setItemName(String pItemName);
	
	/**
	 * Valor de cada Item (o valor total é multiplicado pela Quantity).
	 * @return
	 */
	public Double getValue();
	public void setValue(Double pValue);
	
	/**
	 * Quantidade de Itens.
	 * @return
	 */
	public Integer getQuantity();
	public void setQuantity(Integer pQuantity);
	
	/**
	 * URL de retorno em caso de sucesso na página de autorização de pagamento.
	 * @return
	 */
	public String getSucessURL();
	public void setSucessURL(String pSucessURL);
	
	/**
	 * URL de retorno em caso de cancelamento na página de autorização de pagamento.
	 * @return
	 */
	public String getCancelURL();
	public void setCancelURL(String pCancelURL);
	
	/**
	 * Frequencia do ciclo de uma Periodicidade de um Pagamento Recorrente.
	 * @return
	 */
	public Integer getFrequency();
	public void setFrequency(Integer pFrequency);
	
	/**
	 * Periodicidade de um Pagamento Recorrente.
	 * @return
	 */
	public PERIODICIDADE getPeriod();
	public void setPeriod(PERIODICIDADE pPeriod);
	
	/**
	 * Configura o pagamento inicial em caso de Pagamento Recorrente.
	 * @return
	 */
	public boolean getInitialPayment();
	public void setInitialPayment(boolean pInitialPayment);
	
	/**
	 * Identificação do Profile de Pagamento Recorrente.
	 * @return
	 */
	public String getProfileId();
	public void setProfileId(String pProfileId);
	
	//Actions=======================================================================================================
	/**
	 * Prepara as informações do pagamento e retorna a URL do provedor de pagamentos.<br/>
	 * <ul><b>Eventos disparados</b>
	 * <li>onPrepare()</li>
	 * <li>onRedirect()</li>
	 * </ul>
	 * @return URL alvo para efetuar o pagamento
	 * @throws DBSIOException
	 */
	public String preparePayment() throws DBSIOException;
	
	/**
	 * Após a autorização de pagamento e retorno do site de pagamento, lança o pagamento de fato.
	 * Se for um pagamento recorrente, cria um perfil de assinatura.
	 * Se for um pagamento recorrente e houver um pagamento inicial, lança este pagamento antes de criar o perfil de assinatura.
	 * <ul><b>Eventos disparados</b>
	 * <li>onPay()</li>
	 * <li>onSchedule()</li>
	 * </ul>
	 * @return Retorna um boolean se a operação ocorreu corretamente
	 * @throws DBSIOException
	 */
	public boolean pay() throws DBSIOException;
	
	/**
	 * Recupera o atual status do Profile
	 * @return PROFILE_STATUS Status atual do Profile
	 * @throws DBSIOException
	 */
	public PROFILE_STATUS getProfileStatus() throws DBSIOException;
	
	//TODO atualizar assinatura: updateProfile
	//TODO cancelar assinatura: cancelProfile
}
