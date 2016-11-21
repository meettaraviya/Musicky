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
		u = AppUser.objects.filter(username=request.POST['id'])
		if len(u)!=0:
			user = u[0]
			initdict={}
			for song in request.POST:
				if song!='id' :
					rs = Rating.objects.filter(song_id=song)
					if len(rs)==0:
						r = Rating(value=int(request.POST[song][0]) ,song_id=song)
						user.rating_set.add(r)
						r.save()
					else:
						rs[0].value=request.POST[song][0]
						user.rating_set.update(rs[0])
						rs[0].save()
						
					initdict[song] = int(request.POST[song][0]) 
		 
			
			allusers = AppUser.objects.all()
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
			print(vect_all)

			songs ={}
			for i in vect_all:
				rates = Rating.objects.filter(user=i[1])
				print("Rates")
				print(rates)
				for r in rates:
					if r.song_id in songs and r.song_id not in initdict:
						songs[r.song_id]=songs[r.song_id] + f(i[0],r.value)
					else if r.song_id not in songs:
						songs[r.song_id]=f(i[0],r.value)
			sorted_songs = list(sorted(songs.items(), key=operator.itemgetter(1),reverse=True))
			print(Final)
			print(sorted_songs)
			return JsonResponse(json.dumps(sorted_songs))
	return JsonResponse({'status':[]})


@csrf_exempt
def recommend(request):
	if request.method == 'POST':
		for song in request.POST:	
			s = Song.objects.get(name=song)
			s.rating=request.POST[song]

	return JsonResponse({'songs':[]})
