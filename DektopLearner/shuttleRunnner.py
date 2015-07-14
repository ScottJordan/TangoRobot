from __future__ import print_function

#!/usr/bin/env python


from robotEnvironment import RobotEnvironment
from shuttleRunTask import ShuttleRunTask
from pybrain.rl.agents import LearningAgent
from pybrain.rl.experiments import EpisodicExperiment
from pybrain.rl.learners.valuebased import NFQ, ActionValueNetwork
from pybrain.rl.explorers.discrete import EpsilonGreedyExplorer

from numpy import array, arange, meshgrid, pi, zeros, mean
from matplotlib import pyplot as plt

# switch this to True if you want to see the cart balancing the pole (slower)
render = False

plt.ion()
print("creating environment")
env = RobotEnvironment()
print("Creating Module")
module = ActionValueNetwork(4, 2)
print("Creating Task")
task = ShuttleRunTask(env, 500)
print("Creating Learning")
learner = NFQ()
#learner.explorer = EpsilonGreedyExplorer
print("Creating Agent")
agent = LearningAgent(module, learner)
#testagent = LearningAgent(module, None)
print("Creating Experiment")
experiment = EpisodicExperiment(task, agent)

def plotPerformance(values, fig):
	plt.figure(fig.number)
	plt.clf()
	plt.plot(values, 'o-')
	plt.gcf().canvas.draw()
	# Without the next line, the pyplot plot won't actually show up.
	plt.pause(0.001)

performance = []

#if not render:
#	pf_fig = plt.figure()

while(True):
	print("Starting Episode")
	task.StartEpisode()
	# one learning step after one episode of world-interaction
	experiment.doEpisodes(1)
	agent.learn(1)

	## test performance (these real-world experiences are not used for training)
	#if render:
		#env.delay = True
	#experiment.agent = testagent
	#r = mean([sum(x) for x in experiment.doEpisodes(5)])
	#env.delay = False
	#testagent.reset()
	#experiment.agent = agent
	
#	performance.append(r)
#	if not render:
#		plotPerformance(performance, pf_fig)
        
#	print("reward avg", r)
#	print("explorer epsilon", learner.explorer.epsilon)
#	print("num episodes", agent.history.getNumSequences())
	#print("update step", len(performance))
	user_input = raw_input("Next Episode (y/n): ")
	while(user_input != 'y' and user_input != 'n'):
		user_input = raw_input("Next Episode (y/n): ")
    	
	if user_input == 'y':
		continue
	else:
		env.robotServer.close()
		break


