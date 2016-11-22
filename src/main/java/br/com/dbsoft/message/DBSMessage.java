package br.com.dbsoft.message;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.joda.time.DateTime;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.util.DBSIO;
import br.com.dbsoft.util.DBSObject;

/**
 * @author ricardo.villar
 *
 */
public class DBSMessage implements IDBSMessage{

	private static final long serialVersionUID = 2176781176871000385L;

	private String						wMessageTextOriginal;
	private String						wMessageText;
	private Boolean						wValidated = null; 
	private MESSAGE_TYPE				wMessageType;
	private Exception					wException;
	private String						wMessageTooltip = "";
	private DateTime					wTime;
	private String						wMessageKey = null;
	private Integer						wMessageCode = 0;
	private Set<String>					wMessageSourceIds = new HashSet<String>();
	private Set<IDBSMessageListener> 	wMessageListeners = new HashSet<IDBSMessageListener>();
	
	
	//Construtores============================
	public DBSMessage(){}

	public DBSMessage(DBSIOException e){
		pvSetMessage(e.getLocalizedMessage(), 0, MESSAGE_TYPE.ERROR, e.getLocalizedMessage(), null, null);
	}

	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText){
		pvSetMessage(pMessageText, 0, pMessageType, pMessageText, null,  null);
	}
	
	public DBSMessage(MESSAGE_TYPE pMessageType, Integer pMessageCode, String pMessageText){
		pvSetMessage(pMessageText, pMessageCode, pMessageType, pMessageText, null, null);
	}

	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip){
		pvSetMessage(pMessageText,0, pMessageType, pMessageText, pMessageTooltip, null);
	}

	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText, DateTime pMessageTime){
		pvSetMessage(pMessageText,0, pMessageType, pMessageText, null, pMessageTime);
	}

	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pMessageTime){
		pvSetMessage(pMessageText,0, pMessageType, pMessageText, pMessageTooltip, pMessageTime);
	}

	public DBSMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText){
		pvSetMessage(pMessageKey,0, pMessageType, pMessageText, null, null);
	}
	
	public DBSMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip){
		pvSetMessage(pMessageKey,0, pMessageType, pMessageText, pMessageTooltip, null);
	}
	
	public DBSMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, DateTime pMessageTime){
		pvSetMessage(pMessageKey,0, pMessageType, pMessageText, null, pMessageTime);
	}

	public DBSMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pMessageTime){
		pvSetMessage(pMessageKey,0, pMessageType, pMessageText, pMessageTooltip, pMessageTime);
	}


	

	//=========================================
	
	@Override
	public String getMessageKey(){return wMessageKey; }

	@Override
	public void setMessageKey(String pMessageKey){
		if (pMessageKey != null){
			wMessageKey = pMessageKey.trim();
		}else{
			wMessageKey = pMessageKey;
		}
	}
	
	@Override
	public String getMessageText() {return wMessageText;}
	
	@Override
	public void setMessageText(String pMessageText) {
		//Seta a chave como o próprio texto caso não tenha seja nula.
		if (wMessageKey == null){
			setMessageKey(pMessageText);
		}
		wMessageText = pMessageText;
	}

	@Override
	public MESSAGE_TYPE getMessageType() {return wMessageType;}
	
	/**
	 * Retorna o tipo de mensagem 
	 * @param pMessageType 
	 */
	@Override
	public void setMessageType(MESSAGE_TYPE pMessageType) {wMessageType = pMessageType;}
	
	/**
	 * Código da mensagem.
	 * @return
	 */
	@Override
	public Integer getMessageCode() {return wMessageCode;}

	/**
	 * Retorna o código da mensagem, 
	 * @param pMessageCode
	 */
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setMessageCode(java.lang.Integer)
	 */
	@Override
	public void setMessageCode(Integer pMessageCode) {wMessageCode = pMessageCode;}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#isMessageValidated()
	 */
	@Override
	public Boolean isMessageValidated() {return wValidated;}
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setMessageValidated(java.lang.Boolean)
	 */
	@Override
	public void setMessageValidated(Boolean validated) {
		wValidated = validated;
		pvFireEventAfterMessageValidated();
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#isMessageValidatedTrue()
	 */
	@Override
	public boolean isMessageValidatedTrue() {
		return DBSObject.getNotNull(isMessageValidated(), false);
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#getMessageTooltip()
	 */
	@Override
	public String getMessageTooltip() {return wMessageTooltip;}
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setMessageTooltip(java.lang.String)
	 */
	@Override
	public void setMessageTooltip(String pMessageTooltip) {this.wMessageTooltip = pMessageTooltip;}
	
	/**
	 * Incorpora os parametros a mensagem padrão definida no construtor.<br/>
	 * A mensagem padrão deverá conter o simbolo %s nas posições que se deseja incluir os parametros informados.
	 * @param pParameters
	 */
	@Override
	public void setMessageTextParameters(Object... pParameters){
		if (wMessageTextOriginal != null){
			this.setMessageText(String.format(wMessageTextOriginal, pParameters));
		}else{
			this.setMessageText(String.format(getMessageText(), pParameters));
		}
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#getMessageTime()
	 */
	@Override
	public DateTime getMessageTime() {return wTime;}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setMessageTime(org.joda.time.DateTime)
	 */
	@Override
	public void setMessageTime(DateTime pTime) {wTime = pTime;}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#getIds()
	 */
	@Override
	public Set<String> getMessageSourceIds() {
		return wMessageSourceIds;
	}
	


	@Override
	public IDBSMessage addMessageListener(IDBSMessageListener pMessageListener) {
		if (pMessageListener == null){return this;}
		wMessageListeners.add(pMessageListener);
		return this;
	}

	@Override
	public IDBSMessage removeMessageListener(IDBSMessageListener pMessageListener) {
		if (pMessageListener == null){return this;}
		wMessageListeners.remove(pMessageListener);
		return this;
	}

	@Override
	public Set<IDBSMessageListener> getMessageListeners() {
		return wMessageListeners;
	}

	@Override
	public void copyFrom(IDBSMessage pMessage){
		if (pMessage == null 
         || pMessage.equals(this)){return;}
		DBSIO.copyDataModelFieldsValue(pMessage, this);  
//		setMessageCode(DBSObject.getNotNull(pMessage.getMessageCode(),0));
//		setMessageKey(pMessage.getMessageKey());
//		setMessageText(pMessage.getMessageText());
//		setMessageTime(pMessage.getMessageTime());
//		setMessageTooltip(DBSObject.getNotNull(pMessage.getMessageTooltip(),""));
//		setMessageType(pMessage.getMessageType());
//		setMessageValidated(pMessage.isMessageValidated());
//		getMessageSourceIds().clear();
//		getMessageSourceIds().addAll(pMessage.getMessageSourceIds());
//		getMessageListeners().clear();
//		getMessageListeners().addAll(pMessage.getMessageListeners());
	}

	@Override
	public boolean equals(IDBSMessage pSourceMessage) {
		return equals(pSourceMessage.getMessageKey());
	}

	@Override
	public boolean equals(String pMessageKey) {
		return DBSObject.isEqual(this.getMessageKey(), pMessageKey);
	}
	
	@Override
	public IDBSMessage clone(){
		try {
			IDBSMessage xM = this.getClass().newInstance();
			xM.copyFrom(this);
			return xM;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void reset() {
		wValidated = null;
	}


	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#getException()
	 */
	@Override
	public Exception getException() {return wException;}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setException(java.lang.Exception)
	 */
	@Override
	public void setException(Exception pException) {this.wException = pException;}

	//PROTECTED =========================
	protected void pvSetMessage(String pMessageKey, Integer pMessageCode, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pMessageTime){
		setMessageKey(pMessageKey);
		setMessageCode(pMessageCode);
		setMessageType(pMessageType);
		setMessageText(pMessageText);
		setMessageTooltip(pMessageTooltip);
		setMessageTime(pMessageTime);
		wMessageTextOriginal = pMessageText;
	}

	//PRIVATE =========================
	/**
	 * Dispara evento informando que mensagem foi validada.
	 */
	private void pvFireEventAfterMessageValidated(){
		Iterator<IDBSMessageListener> xI = getMessageListeners().iterator(); 
		while(xI.hasNext()){
			IDBSMessageListener xListener = xI.next();
			xListener.afterMessageValidated(this);
//			System.out.println("pvFireEventAfterMessageValidated\t" + xListener.toString());
		}
		wMessageListeners.clear();
	}



}
