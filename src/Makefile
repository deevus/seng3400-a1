CLIENT_SRC := CodeClient.java
SERVER_SRC := CodeConverter.java
OTHER_SRC := CodeProtocol.java CodeProtocolState.java
OUTDIR := ../bin
JC := javac
JFLAGS := -g -d $(OUTDIR)
MKDIR := mkdir
MDFLAGS := -p

all: init client server

init:
	$(MKDIR) $(MDFLAGS) $(OUTDIR)

client:
	$(JC) $(JFLAGS) $(CLIENT_SRC)

server:
	$(JC) $(JFLAGS) $(SERVER_SRC) $(OTHER_SRC)
