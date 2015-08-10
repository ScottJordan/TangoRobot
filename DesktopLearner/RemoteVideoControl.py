import sys
from select import select
import time
import math
#sys.path.append("./F310_Gamepad_Parser/core")
from F310_Gamepad_Parser.core.bus import *
from F310_Gamepad_Parser.core.parser_core import *
from logger import Logger
from robotServer import RobotServer

class RemoteControl:
	# Create bus object
	bus = Bus()
	# Create a dictionary to be used to keep states from joy_core
	states = { 'A':0, 'B':0, 'X':0, 'Y':0,  		   		\
		'Back':0, 'Start':0, 'Middle':0,		   			\
		'Left':0, 'Right':0, 'Up':0, 'Down':0, 				\
		'LB':0, 'RB':0, 'LT':0, 'RT':0,						\
		'LJ/Button':0, 'RJ/Button':0, 						\
		'LJ/Left':0, 'LJ/Right':0, 'LJ/Up':0, 'LJ/Down':0, 	\
		'RJ/Left':0, 'RJ/Right':0, 'RJ/Up':0, 'RJ/Down':0,	\
		'Byte0':0, 'Byte1':0, 'Byte2':0, 'Byte3':0,			\
		'Byte4':0, 'Byte5':0, 'Byte6':0, 'Byte7':0,			\
		'Byte0/INT':0, 'Byte1/INT':0, 'Byte2/INT':0, 		\
		'Byte3/INT':0, 'Byte4/INT':0, 'Byte5/INT':0, 		\
		'Byte6/INT':0, 'Byte7/INT':0}
		
	checkController = True
	def __init__(self):
		self.robotServer = RobotServer()
		#self.robotServer.getConnection()
		self.parsercore = ParserCore(self.bus, self.states, self.checkController)
		self.logger = Logger("robotImagesLog.txt")
		self.logger.writeStartOfSequence()

	def _scalePower(self, power, lowLim, upLim):
		if(power == 0):
			return 0
		elif(abs(power) > 120):
			return math.copysign(upLim, power)
		else:
			per = (float(abs(power) - lowLim)/float(120 - lowLim))
			scalp = per * float(upLim- lowLim) + lowLim
			return math.copysign(scalp, power)
			
	def scaleMotorPower(self, left, right):
		scaledLeft  = self._scalePower(left, 10, 40)
		scaledRight = self._scalePower(right, 10, 40)
		return scaledLeft, scaledRight
		
	def scalePowerForLog(self, left, right):
		logLeft = float(left)/40.0
		logRight = float(right)/40.0
		return logLeft, logRight
		
	def main(self):
   		timeout = 0.005
   		self.robotServer.getConnection()
   		print("robotConnected")
   		self.parsercore.start()
   		print("controller started")
   		self.quit = False
   		self.robotServer.readMessage()
   		self.robotServer.sendStart()
   		print('Enter Q to quit: ')
   		starttime = time.time()
   		loops = 0
   		while(self.quit == False):
			fname = self.robotServer.readImage()
   			rlist, _, _ = select([sys.stdin], [], [], timeout)
			if rlist:
				print("input received")
				s = sys.stdin.readline()
				if(s == 'Q'):
					print("Q detected. Quiting..")
					self.quit = True
					self.checkController = False
					self.parsercore.join()
				else:
					print("Not 'Q'\nEnter Q to quit:")
					
			leftPower = self.states['LJ/Up'] + self.states['LJ/Down']
			rightPower = self.states['RJ/Up'] + self.states['RJ/Down']
			leftPower, rightPower = self.scaleMotorPower(leftPower, rightPower)
			logLeft , logRight = self.scalePowerForLog(leftPower, rightPower)
			self.logger.log(fname + " " + str(logLeft) + " " + str(logRight))
			#print(leftPower, rightPower)
			sent = self.robotServer.sendAction(int(leftPower), int(rightPower))
			loops += 1
			nowtime = time.time()
			fps = float(loops) / (nowtime - starttime)
			print("FPS: " + str(fps) + " Number of frames: " + str(loops) + " total time: " + str(nowtime-starttime)) 
		
		self.robotServer.close()

if __name__ == "__main__":
    RemoteControl().main()	
