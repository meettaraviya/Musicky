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
		imp = list(request.POST.values())
		userid = imp.pop()
		a = AppUser(username=userid)
		print(a)
		# a.save()
		# for i in imp :
		# 	a.add(genre=i)		
		g=Genre.objects.filter(name__in=imp)
		songs = Song.objects.filter(genre__in=g)
		x = serializers.serialize('json',songs)		
		return HttpResponse(x)
	return JsonResponse({'songs':[]})

@csrf_exempt
def recommend(request):
	if request.method == 'POST':
		for song in request.POST:	
			s = Song.objects.get(name=song)
			s.rating=request.POST[song]

	return JsonResponse({'songs':[]})