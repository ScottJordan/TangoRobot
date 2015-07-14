class Logger:
	filename = 'log.txt'
	logfile = None
	logList = []
	
	def open(self):
		self.logfile = open(self.filename, 'a')
	
	def writeStartOfSequence(self):
		self.open()		
		self.logfile.write("Start Episode\n")
		self.logfile.close()
	
	def writeEndOfSequence(self):
		self.open()		
		self.logfile.write("End Episode\n")
		self.logfile.close()
	
	def log(self, entry):
		self.logList.append(entry + "\n")
		if(len(self.logList) >= 100):
			self.write()
	
	def write(self):
		self.open()
		for entry in self.logList:
			self.logfile.write(entry)
		del self.logList[:]
		self.logfile.close()
	
	def __init__(self):
		self.filename="log.txt"
	
	def __init__(self, name):
		self.filename=name
