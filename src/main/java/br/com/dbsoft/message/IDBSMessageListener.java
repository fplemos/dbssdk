package br.com.dbsoft.message;


public interface IDBSMessageListener {
	
	/**
	 * Disparado após a mensagem ser validada.
	 * @param pMessage
	 * @return
	 */
	public <MessageClass extends IDBSMessage> void afterMessageValidated(MessageClass pMessage);

}
