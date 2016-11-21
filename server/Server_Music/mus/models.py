from django.db import models

class Genre(models.Model):
   name = models.TextField(max_length=50) 

# class Artist(models.Model):
#    name = models.TextField(max_length=50) 

class Song(models.Model):
	genre = models.ForeignKey(Genre)
	rating = models.SmallIntegerField(default=0)
	artist = models.TextField(max_length=50)
	name = models.TextField(max_length=50)
	url = models.TextField(max_length=50)
	