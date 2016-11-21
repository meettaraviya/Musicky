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
		g=Genre.objects.filter(name__in=list(request.POST.values))
		songs = Song.objects.filter(genre__in=g)
	 	x = serializers.serialize('json',songs)		
		return HttpResponse(x)
	return JsonResponse({'songs':[]})

# @csrf_exempt
# def recommend(request):
# 	if request.method == 'POST':
# 		json = demjson.decode(request.POST)
# 		for song in json:
# 			Song.objects.get(name=song) 
# 				songs = Song.objects.filter(genre=g)
# 				x = serializers.serialize('json',songs)
# 				return HttpResponse(x)
# 	return JsonResponse({'songs':[]})