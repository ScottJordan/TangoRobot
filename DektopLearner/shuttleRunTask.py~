from pybrain.rl.environments import EpisodicTask
from robotEnvironment import RobotEnvironment
from logger import Logger
import numpy as np

class ShuttleRunTask(EpisodicTask):
	logger = Logger('shuttleRunLog.txt')
	# The current real values of the state
	cur_pos = 0.0
	checkpoint = 0
	lap = 0
	motorpower = 0.0
	state = [cur_pos, checkpoint, lap, motorpower]
	#The number of actions.
	move_fwd = [20 , 20]
	move_back = [-20 , -20]
	action_list = np.int_([move_fwd, move_back])
	nactions = len(action_list)

	nsenses = 4

	# number of steps of the current trial
	steps = 0

	# number of the current episode
	episode = 0

	# Goal Position
	frontGoalPos = 0.75
	backGoalPos = -0.75
	numLaps = 3
	maxSteps = 999

	resetOnSuccess = True

	def __init__(self, environment, maxSteps):
		self.env = environment
		self.nactions = len(self.action_list)
		self.reset()
		self.cumreward = 0
		self.maxSteps = maxSteps
		self.logger.open()

	def reset(self):
		self.state = self.GetInitialState()

	def getObservation(self):    
		sensors = self.env.getSensors()
		self.cur_pos = sensors[2]
		self.motorpower = (sensors[7] + sensors[8]) / 20 #scale motor power
		self.GetReward()
		return self.state
		
	def performAction(self, action):
		self.DoAction(action)

	def getReward(self):
		return self.r    

	def GetInitialState(self):
	#		self.StartEpisode()
		self.cumreward = 0
		return [0.0, 0, 0, 0.0]

	def StartEpisode(self):
		self.steps = 0
		self.episode = self.episode + 1
		self.logger.writeStartOfSequence()
		status = self.env.start()
		self.env.updateSensors()
		
	def isFinished(self):
		if((self.lap > 3 and self.cur_pos >= 0) or self.steps > self.maxSteps):
			self.env.end()
			self.logger.write()
			self.logger.writeEndOfSequence()
			return True
		else:
			return False


	def GetReward(self):
		if (self.checkpoint == 0):
			if (self.cur_pos < .75):
				self.r = 1000 - ((abs(.75 - self.cur_pos) / 1.5) * 1000.0)
			elif (self.cur_pos >= .75):
				self.r = 1000
				self.checkpoint = 1
			else:
				self.r = 0
		elif (self.checkpoint == 1):
			if (self.cur_pos > -.75):
				self.r = 1000 - ((abs(-.75 - self.cur_pos) / 1.5) * 1000.0);
			elif (self.cur_pos <= -.75):
				self.r = 1000
				self.checkpoint = 0
				self.lap += 1
			else:
				self.r = 0     
		self.cumreward += self.r


	def DoAction(self, a):
		#print('action',a)
		#print('state',s)
		action = self.action_list[int(a)]
		self.env.performAction(action)
		self.steps = self.steps + 1

	def logState(self, state, action, reward, cumReward):
		state_string = ""		
		for val in state:
			state_string += str(val) + ","
		state_string += str(reward) + "," + str(cumReward) + ","
		state_string += str(action)
		self.logger.log(state_string)
