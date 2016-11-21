from django.db import models
from django.contrib.auth.models import User

class Genre(models.Model):
	name = models.TextField(max_length=50,primary_key=True)
	def __str__(self):
		return "%s" %(self.name)
# class Artist(models.Model):
#    name = models.TextField(max_length=50) 
	
class Song(models.Model):
	genre = models.ForeignKey(Genre)
	artist = models.TextField(max_length=50)
	name = models.TextField(max_length=50,primary_key=True)

	def __str__(self):
		return "%s" %(self.name)

class AppUser(models.Model):
	username =models.TextField(max_length=50,primary_key=True,default="1")
	genres = models.ManyToManyField(Genre)
	songs = models.ManyToManyField(Song)
	def __str__(self):
		return "%s" %(self.username)

class Rating(models.Model):
	user = models.ForeignKey(AppUser)
	song_id = models.TextField(max_length=50)
	value = models.SmallIntegerField()
	def __str__(self):
		return "%s and %s : %s" %(self.name, self.song_id, self.value)
