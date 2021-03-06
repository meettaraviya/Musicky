# -*- coding: utf-8 -*-
# Generated by Django 1.11 on 2016-11-21 17:22
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('mus', '0003_appuser'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='appuser',
            name='id',
        ),
        migrations.RemoveField(
            model_name='appuser',
            name='user',
        ),
        migrations.RemoveField(
            model_name='genre',
            name='id',
        ),
        migrations.RemoveField(
            model_name='song',
            name='id',
        ),
        migrations.RemoveField(
            model_name='song',
            name='url',
        ),
        migrations.AddField(
            model_name='appuser',
            name='username',
            field=models.TextField(default='1', max_length=50, primary_key=True, serialize=False),
        ),
        migrations.AlterField(
            model_name='genre',
            name='name',
            field=models.TextField(max_length=50, primary_key=True, serialize=False),
        ),
        migrations.AlterField(
            model_name='song',
            name='name',
            field=models.TextField(max_length=50, primary_key=True, serialize=False),
        ),
    ]
