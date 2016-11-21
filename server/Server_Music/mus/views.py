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

@csrf_exempt
def getPreferenceList(request):
	if request.method == 'POST':
		print(request.POST)
		userid = request.POST['id']
		imp=[]
		for i in request.POST:
			imp.append(request.POST[i])
		a = AppUser(username=userid)
		a.save()
		print(a)
		g=Genre.objects.filter(name__in=imp)
		for gen in g:
			print(gen)
			a.genres.add(gen)
		songs = Song.objects.filter(genre__in=g)
		print(songs)
		x = serializers.serialize('json',songs)
	return HttpResponse(x)
	return JsonResponse({	'songs':[]})

@csrf_exempt
def rate(request):
	if request.method == 'POST':
		print(request.POST)
		u = AppUser.objects.filter(username=request.POST['id'])
		print(u)
		if len(u)!=0:
			user = u[0]
			print(user)
			initdict={}
			for song in request.POST:
				if song!='id' :
					r = Rating(value=int(request.POST[song][0]) ,song_id=song)
					print(r)
					user.rating_set.add(r)
					r.save()
					initdict[song] = int(request.POST[song][0]) 
		 
			allusers = AppUser.objects.all()
			vect_all=[]
			for us in allusers:
				print(us)
				vect_us=0
				rates=Rating.objects.filter(user=us)
				for s in initdict:
					print(s)
					r =rates.objects.filter(song_id=s)
					if len(r) !=0 :
						print(r)
						vect_us=vect_us+(initdict[s] - r.value)**2 
				vect_all.append({us.username:vect_us})
			print(vect_all)
			sorted(vect_all, key=lambda x:vect_all[x])
			print(vect_all)
			return JsonResponse({'status':'yes'})
	return JsonResponse({'status':'no'})


@csrf_exempt
def recommend(request):
	if request.method == 'POST':
		for song in request.POST:	
			s = Song.objects.get(name=song)
			s.rating=request.POST[song]

	return JsonResponse({'songs':[]})
