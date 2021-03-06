from django.shortcuts import render
from django.shortcuts import render, render_to_response
from django.template import RequestContext
from django.http import HttpResponse, HttpResponseRedirect
from .models import *

# Create your views here.
from django.http import JsonResponse
from django.middleware.csrf import get_token
from django.core import serializers
from django.views.decorators.csrf import ensure_csrf_cookie
from django.views.decorators.csrf import csrf_exempt
import json
from itertools import chain
import operator

@csrf_exempt
def getPreferenceList(request):
	if request.method == 'POST':
		userid = request.POST['id']
		imp=[]
		for i in request.POST:
			imp.append(request.POST[i])
		a = AppUser(username=userid)
		a.save()
		g=Genre.objects.filter(name__in=imp)
		for gen in g:
			print(gen)
			a.genres.add(gen)
		songs = Song.objects.filter(genre__in=g)
		x = serializers.serialize('json',songs)
		return HttpResponse(x)
	return JsonResponse({	'songs':[]})

def f(x,r):
	return r/(1 + x**0.5)

@csrf_exempt
def rate(request):
	if request.method == 'POST':
		u = AppUser.objects.filter(username=request.POST.get('id'))	
		if len(u)!=0:
			user = u[0]
			print(request.POST)
			initdict={}
			for song in request.POST:
				if song!='id' :
					rs = Rating.objects.filter(song_id=song,user=user)
					print("RRSS::");print(rs)
					if len(rs)==0:
						r = Rating(value=int(request.POST[song]) ,song_id=song)
						user.rating_set.add(r)
						r.save()
					else:
						rs[0].value=int(request.POST[song])
						user.rating_set.filter(song_id=song).update(value=int(request.POST[song]))
						rs[0].save()

					initdict[song] = int(request.POST[song]) 
			
			allusers = AppUser.objects.exclude(username=user.username)
			vect_all=[]
			for us in allusers:
				vect_us=0
				rates=Rating.objects.filter(user=us)
				for s in initdict:
					r =rates.filter(song_id=s)
					if len(r) !=0 :
						vect_us=vect_us+(initdict[s] - r[0].value)**2 
				vect_all.append((vect_us,us))
			vect_all=sorted(vect_all,key= lambda x:x[0])
			print('vect_all');print(vect_all)

			songs ={}
			for i in vect_all:
				rates = Rating.objects.filter(user=i[1])
				for r in rates:
					print("Rates");print(r)
				
					if r.song_id in songs and r.song_id not in initdict:
						songs[r.song_id]=songs[r.song_id] + f(i[0],r.value)
					elif r.song_id not in songs and r.song_id not in initdict:
						songs[r.song_id]=f(i[0],r.value)
					print(songs)
			
			sorted_songs = sorted(songs.items(), key=operator.itemgetter(1),reverse=True)
			dict_sorted=dict(sorted_songs)
			print('Final');print(sorted_songs)
			
			song_cur=Song.objects.filter(name__in=list(dict_sorted.keys()))
			song_req=song_cur.filter(genre__in=list(user.genres.all()))
			def func(s): 
				return s.song_id

			print("USER:::");print(user)
			rate_list= user.rating_set.all()
			print(rate_list)
			ss = list(map(func,rate_list))
			print(ss)
			song_list=Song.objects.filter(name__in=ss)

			y=serializers.serialize('json',song_list)
			x = serializers.serialize('json',song_req)

			return HttpResponse('{ \"recom\": '+x+', \"mysongs\": '+y+' }')
	
	return JsonResponse({'status':[]})

import csv
from random import randint
@csrf_exempt
def recommend(request):	
	dataReader = csv.reader(open('mus/names.csv'), delimiter=',', quotechar='"')
	gs = ['Pop','EDM','Jazz','Rock']
	for row in dataReader:
		g = Genre.objects.get(name=	gs[randint(0,3)])
		song = Song(name=row[1],artist=row[0])
		song.genre = g
		song.save()
	return HttpResponse ("Data saved")