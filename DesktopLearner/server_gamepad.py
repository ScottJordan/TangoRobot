import socket               # Import socket module
import time
import sys
sys.path.append("./F310_Gamepad_Parser/core")
import threading
from bus import *
from parser_core import *
from logger import Logger

soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)         # Create a socket object
host = "192.168.0.29" # Get local machine IP
port = 9999                # Reserve a port for your service.
soc.bind((host, port))       # Bind to the port
soc.listen(5)                 # Now wait for client connection.

log = Logger()

# Create bus object
bus = Bus()
# Create a dictionary to be used to keep states from joy_core
states = { 'A':0, 'B':0, 'X':0, 'Y':0,  		   		\
		'Back':0, 'Start':0, 'Middle':0,		   	\
		'Left':0, 'Right':0, 'Up':0, 'Down':0, 			\
		'LB':0, 'RB':0, 'LT':0, 'RT':0,				\
		'LJ/Button':0, 'RJ/Button':0, 				\
		'LJ/Left':0, 'LJ/Right':0, 'LJ/Up':0, 'LJ/Down':0, 	\
		'RJ/Left':0, 'RJ/Right':0, 'RJ/Up':0, 'RJ/Down':0,	\
		'Byte0':0, 'Byte1':0, 'Byte2':0, 'Byte3':0,		\
		'Byte4':0, 'Byte5':0, 'Byte6':0, 'Byte7':0,		\
		'Byte0/INT':0, 'Byte1/INT':0, 'Byte2/INT':0, 		\
		'Byte3/INT':0, 'Byte4/INT':0, 'Byte5/INT':0, 		\
		'Byte6/INT':0, 'Byte7/INT':0}
# Launch Parser_Core as a seperate thread to parse the gamepad
parsercore = ParserCore(bus, states)
parsercore.start()

while True:
	conn, addr = soc.accept()     # Establish connection with client.
	print("Got connection from", addr)	

	print("reading message")	
	msg = conn.recv(1024)
	log.log(msg)	
	#print (msg)
	leftMotorPower = states['LJ/Up'] + states['LJ/Down']
	rightMotorPower = states['RJ/Up'] + states['RJ/Down']
	log.log(msg + "," + str(leftMotorPower) + "," + str(rightMotorPower))	
	bsent = conn.send(("L" + str(leftMotorPower) + "R" + str(rightMotorPower) + "\n").encode("UTF-8"))
	if(bsent == None):		
		print("message send success") 
	else:
		print("Not Android Message")

	conn.close()
