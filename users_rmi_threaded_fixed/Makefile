all:			Client.class ClientThread.class \
			Server.class ServerInterface.class

Client.class:		ClientThread.class Client.java
			@javac Client.java

ClientThread.class:	ClientThread.java
			@javac ClientThread.java

Server.class:		Server.java
			@javac Server.java

ServerInterface.class:	ServerInterface.java
			@javac ServerInterface.java

clean:
			@rm -f *.class *~
