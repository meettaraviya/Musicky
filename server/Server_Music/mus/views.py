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

@csrf_exempt
def getPreferenceList(request):
	if request.method == 'POST':
		print(request.POST)
		genre = request.POST.get('genre')
		print(genre)
		g = Genre.objects.filter(name=genre)
		print(len(g))
		if len(g) != 0:
			songs = Song.objects.filter(genre=g)
			x = serializers.serialize('json',songs)
			return HttpResponse(x)
	return JsonResponse({'songs':[]})