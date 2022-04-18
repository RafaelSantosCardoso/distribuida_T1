all:				p2pServer.class p2pPeer.class

Peer: Peer.java
	@javac Peer.java

Resource: Resource.java
	@javac Resource.java

ServerInterface: ServerInterface.java
	@javac ServerInterface.java

p2pServer.class:		p2pServer.java
				@javac p2pServer.java

p2pPeer.class:			p2pPeerThread.class p2pPeerHeartbeat.class p2pPeerClient.class p2pPeer.java
				@javac p2pPeer.java

p2pPeerThread.class:		p2pPeerThread.java
				@javac p2pPeerThread.java
				
p2pPeerHeartbeat.class:		p2pPeerHeartbeat.java
				@javac p2pPeerHeartbeat.java

p2pPeerClient.class:		p2pPeerClient.java
				@javac p2pPeerClient.java

clean:
				@rm -rf *.class *~
