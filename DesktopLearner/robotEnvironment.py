__author__ = 'Thomas Rueckstiess, ruecksti@in.tum.de'

from matplotlib.mlab import rk4
from math import sin, cos
import time
from scipy import eye, matrix, random, asarray

from pybrain.rl.environments import Environment

from robotServer import RobotServer

class RobotEnvironment(Environment):
	indim = 2
	outdim = 9
	robotstarted = False

	def __init__(self, polelength=None):
		self.robotServer = RobotServer()
		self.robotServer.getConnection()
		self.sensors = [0,0,0,0,0,0,0,0,0]
		self.action = [0,0]

	def getSensors(self):
		return self.sensors	

	def updateSensors(self):
		state = self.robotServer.getRobotState()
		stateVars = state.split(",")
		stateVars = map(float, stateVars)
		self.sensors = stateVars

	def performAction(self, action):
		self.action = action
		self.step()

	def step(self):
		self.robotServer.sendAction(self.action[0], self.action[1])
		self.updateSensors()

	def reset(self):
		self.sensors = [0,0,0,0,0,0,0,0,0]
		self.action = [0,0]
	
	def start(self):
		msg = self.robotServer.readMessage()
		if(msg == "Start?"):
			self.robotServer.sendStart()
			msg = self.robotServer.readMessage()
			if(msg == "Starting"):
				return True
	def end(self):
		self.robotServer.sendEnd()

		
