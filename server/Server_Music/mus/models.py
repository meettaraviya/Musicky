from django.db import models
from django.contrib.auth.models import User

class Genre(models.Model):
   name = models.TextField(max_length=50) 

# class Artist(models.Model):
#    name = models.TextField(max_length=50) 

class AppUser(models.Model)
	user = models.OneToOneField(User, on_delete=models.CASCADE)
	genres = models.ManyToManyField(Genre)
	songs = models.ManyToManyField(Song)
	
class Song(models.Model):
	genre = models.ForeignKey(Genre)
	rating = models.SmallIntegerField(default=0)
	artist = models.TextField(max_length=50)
	name = models.TextField(max_length=50)
	url = models.TextField(max_length=50)
	