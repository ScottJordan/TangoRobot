import socket               # Import socket module
import time
from logger import Logger

class RobotServer:
	soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)	# Create a socket object
	host = "192.168.0.29" # Get local machine IP
	port = 9999           # Reserve a port for your service.
	packetSize = 1024

	def __init__(self):
		self.soc.bind((self.host, self.port))       # Bind to the port
		self.soc.listen(5)                # Now wait for client connection.

	def getConnection(self):
		self.conn, self.addr = self.soc.accept()

	def readMessage(self):
		msg = self.conn.recv(1024)
		return msg

	def sendMessage(self, msg):
		sent = self.conn.send((msg + "\n").encode("UTF-8"))
		if(sent == None):
			return True
		else:
			return False

	def getRobotState(self):
		msg = self.readMessage()
		if not msg:
			self.getConnection()
			msg = readMessage()
		return msg

	def sendAction(self, leftMotorPower, rightMotorPower):
		msg = "L" + str(leftMotorPower) + "R" + str(rightMotorPower)
		self.sendMessage(msg)
		
	def sendStart(self):
		self.sendMessage("Start")

	def sendEnd(self):
		self.sendMessage("End")
	
	def close(self):
		self.sendEnd()
		self.soc.close()
