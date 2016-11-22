# Full path and name to your csv file
csv_filepathname="names.csv"
# Full path to your django project directory
your_djangoproject_home= "C:\\Users\Rishabh\Documents\musicky\server\Server_Music"
import sys,os
from random import randint
sys.path.append(your_djangoproject_home)
os.environ['DJANGO_SETTINGS_MODULE'] = 'settings'
from mus.models import *
import csv
dataReader = csv.reader(open(csv_filepathname), delimiter=',', quotechar='"')
gs = ['Pop','EDM','Jazz','Rock']
for row in dataReader:
	g = Genre.objects.get(name=gs[randint(0,3)])
	song = Song(name=row[1],artist=row[0])
	song.genre = g
	song.save()