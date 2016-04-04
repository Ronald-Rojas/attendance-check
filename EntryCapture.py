import RPi.GPIO as GPIO
import time
import datetime
from subprocess import call
#The Following is the start up of the program

curl -x POST "https://api.clarifai.com/v1/token/" \
        -d "client_id={client_id}" \
        -d "client_secret={client_secret}"\
        -d "grant_type=client_credentials"

classinformation = [[0 for i in xrange(4)] for i in xrange(5)]
classinformation[0][0] = "cs404"
classinformation[0][1] = datetime.date(2016,4,3)
classinformation[0][2] = datetime.time(12,30)
classinformation[0][3] = datetime.time(12,30)
def checkDistance():
    GPIO.output(TRIG,1)
    time.sleep(0.00001)
    GPIO.output(TRIG,0)

    while GPIO.input(ECHO) ==0:
        pass
    start = time.time()

    while GPIO.input(ECHO) == 1:
        pass
    stop = time.time()

    return (stop - start) * 170


GPIO.setmode(GPIO.BOARD)

TRIG = 7
ECHO = 12

GPIO.setup(TRIG,GPIO.OUT)
GPIO.output(TRIG,0)

GPIO.setup(ECHO,GPIO.IN)
time.sleep(0.1)


GPIO.output(TRIG,1)
time.sleep(0.00001)
GPIO.output(TRIG,0)


doorDistance = checkDistance()

print "Setup has finished"
print doorDistance


classID = (raw_input("what is your classID for today?"))
classIndex = 0
while classID != classinformation[classIndex][0]:
    classIndex= classIndex +1
    print "adding to class index"
currentTime = datetime.time(datetime.datetime.now().hour,datetime.datetime.now().minute)

while currentTime < classinformation[classIndex][3]:
    time.sleep(0.1)
    if checkDistance > doorDistance +.5 or checkDistance < doorDistance +.5:
        call(["raspistill -ss .1 -o students.jpeg"])
        os.system(EntryTags.py)

GPIO.cleanup()
