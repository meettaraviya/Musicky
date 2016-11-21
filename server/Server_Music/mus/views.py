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
		# songs = Song.objects.filter(genre__in=g)
		# print(songs)
		# x = serializers.serialize('json',songs)		
		return HttpResponse("hi")
	return JsonResponse({'songs':[]})

@csrf_exempt
def rate(request):
	if request.method == 'POST':
		for song in request.POST:	
			s = Song.objects.get(name=song)
			s.rating=request.POST[song]
			s.save()
		return JsonResponse({'status':'yes'})
	return JsonResponse({'status':'no'})

@csrf_exempt
def recommend(request):
	if request.method == 'POST':
		for song in request.POST:	
			s = Song.objects.get(name=song)
			s.rating=request.POST[song]

	return JsonResponse({'songs':[]})