<script lang="ts">
    import {onMount} from "svelte";
    import "../app.css";
    import Icon from "@iconify/svelte";

    import FoodCard from './FoodCard.svelte';
    import MealFilter from './MealFilter.svelte';
    import type {Meal} from '$lib/types';
    import type {PageProps} from "./$types";
    import { BaseURL } from '$lib/env';
    import { getCookie } from '$lib';

    let {data}: PageProps = $props();

    // For more information on runes and reactivity, see: https://svelte.dev/docs/svelte/what-are-runes
    let meals: Meal[] = $state(data.meals);
    let recommendation: string | undefined = data.recommendation?.recommendation;
    let menuFilterEnabled: boolean = data.menuFilterEnabled;

    // Filter functionality (only active when feature toggle is enabled)
    let searchQuery: string = $state("");
    let showFilterPanel: boolean = $state(false);

    // Filter states
    let dietaryFilters = $state({
        vegetarian: false,
        vegan: false,
        containsMeat: false,
    });

    let filteredMeals: Meal[] = $derived(
        menuFilterEnabled ? meals.filter((meal) => {
            // Search query filter
            const searchMatch =
                searchQuery === "" ||
                meal.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
                meal.labels.some((label: string) =>
                    label.toLowerCase().includes(searchQuery.toLowerCase()),
                );

            // Dietary preference filters
            const dietaryMatch =
                (!dietaryFilters.vegetarian ||
                    meal.labels.includes("VEGETARIAN")) &&
                (!dietaryFilters.vegan || meal.labels.includes("VEGAN")) &&
                (!dietaryFilters.containsMeat ||
                    meal.labels.includes("MEAT") ||
                    meal.labels.includes("PORK"));

            return searchMatch && dietaryMatch;
        }) : meals
    );
</script>

<main>
    <header>
        <h1>Garching Campus Canteen</h1>
        <p>Today's menu offerings</p>
    </header>

    <!-- Recommendation Banner -->
    {#if recommendation}
        <div class="recommendation-banner">
            <div class="recommendation-content">
                <h3>🤖 AI Recommendation</h3>
                <p>{recommendation}</p>
            </div>
        </div>
    {:else}
        <div class="recommendation-banner empty">
            <div class="recommendation-content">
                <h3>🤖 AI Recommendation</h3>
                <p>No recommendations available. Try adding some favorite meals first!</p>
            </div>
        </div>
    {/if}

    <!-- Search and Filter UI (only shown when feature toggle is enabled) -->
    <!-- TODO only show when menuFilterEnabled is true, see below for if Svelte syntax -->


    {#if menuFilterEnabled}
        <MealFilter
            bind:searchQuery={searchQuery}
            bind:showFilterPanel={showFilterPanel}
            bind:dietaryFilters={dietaryFilters}
        />
    {/if}

    {#if meals.length === 0}
        <div class="no-results">
            <p>Loading menu items...</p>
        </div>
    {:else}
        <div class="food-grid">
            {#each meals as meal, i}
                <!-- need to bind original meal object due to favorites and reactivity -->
                {#if filteredMeals.includes(meal)}
                    <FoodCard bind:meal={meals[i]}/>
                {/if}
            {/each}
        </div>
    {/if}

    {#if menuFilterEnabled && filteredMeals.length === 0 && meals.length > 0}
        <div class="no-results">
            No menu items match your filters. Try changing your selection.
        </div>
    {/if}
</main>
