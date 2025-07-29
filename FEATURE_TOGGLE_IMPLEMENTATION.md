# Feature Toggle Implementation Guide

## Overview
This document provides step-by-step instructions for implementing the feature toggle system to control the menu filter functionality in the Garching Campus Canteen application.

## Current State ✅ COMPLETED
- Backend controller is fully implemented with working GET and PUT endpoints
- Frontend successfully fetches feature toggle state from the API
- Menu filter UI is conditionally rendered based on the feature toggle state
- All TODOs have been resolved and the system is working as designed

## Previous State (Before Implementation)
- Backend controller exists but methods are not fully implemented
- Frontend has placeholder code with TODOs for feature toggle integration
- The `menuFilterEnabled` variable is hardcoded to `false`

## Implementation Steps

### 1. Backend Implementation (Java Spring)

#### Fix the PUT endpoint in `FeatureToggleController.java` ✅ COMPLETED
**File:** `server/src/main/java/de/tum/aet/devops25/w10/controller/FeatureToggleController.java`

**Implementation status:** ✅ Both GET and PUT endpoints are fully implemented and working correctly.

**Current implementation:**
```java
@GetMapping("/features/{featureName}")
public ResponseEntity<Boolean> getFeatureToggleState(@PathVariable("featureName") String featureName) {
    return ResponseEntity.ok(featureToggles.getOrDefault(featureName, false));
}

@PutMapping("/features/{featureName}")
public ResponseEntity<Boolean> setFeatureToggleState(@PathVariable("featureName") String featureName, @RequestParam("enabled") Boolean enabled) {
    featureToggles.put(featureName, enabled);
    return ResponseEntity.ok(enabled);
}
```

### 2. Frontend Implementation (Svelte)

#### Update the page loader to fetch feature toggle state ✅ COMPLETED
**File:** `client/src/routes/+page.ts`

**Implementation status:** ✅ Feature toggle fetching is fully implemented and working.

**Current implementation:**
```typescript
const [mealsResult, preferencesResult, recommendationResult, featureToggleResult] = await Promise.allSettled([
    fetch(`${BaseURL}/mensa-garching/today`).then(res => res.json()),
    fetch(`${BaseURL}/preferences/${username}`).then(res => res.json()),
    fetch(`${BaseURL}/recommend/${username}`).then(res => res.json()),
    fetch(`${BaseURL}/features/menu-filter`).then(res => res.json())
]);

// Extract successful results or provide defaults
const meals: Meal[] = mealsResult.status === 'fulfilled' ? mealsResult.value : [];
const preferences: UserPreferences = preferencesResult.status === 'fulfilled' ? preferencesResult.value : { favoriteMeals: [] };
const recommendation: Recommendation | null = recommendationResult.status === 'fulfilled' ? recommendationResult.value : null;
const menuFilterEnabled: boolean = featureToggleResult.status === 'fulfilled' ? featureToggleResult.value : false;
```

#### Add the filter UI in the Svelte component ✅ COMPLETED
**File:** `client/src/routes/+page.svelte`

**Implementation status:** ✅ Conditional rendering of MealFilter component is fully implemented.

**Current implementation:**
```html
{#if menuFilterEnabled}
    <MealFilter
        bind:searchQuery={searchQuery}
        bind:showFilterPanel={showFilterPanel}
        bind:dietaryFilters={dietaryFilters}
    />
{/if}
```

**Additional features implemented:**
- Filtered meals logic that respects the feature toggle
- "No results" message that only shows when filtering is enabled
- Complete integration with search and dietary filters

## API Endpoints

### GET /features/{featureName}
- **Purpose:** Retrieve the current state of a feature toggle
- **Example:** `GET /features/menu-filter`
- **Response:** `true` or `false`

### PUT /features/{featureName}
- **Purpose:** Set the state of a feature toggle
- **Example:** `PUT /features/menu-filter`
- **Body:** `true` or `false`
- **Response:** Updated state (`true` or `false`)

## Feature Names
- `menu-filter`: Controls the visibility and functionality of the menu filtering UI

## Testing the Implementation

### 1. Enable the feature toggle
```bash
curl -X PUT http://localhost:8080/features/menu-filter \
  -H "Content-Type: application/json" \
  -d "true"
```

### 2. Verify the feature toggle state
```bash
curl http://localhost:8080/features/menu-filter
```

### 3. Test in the UI
1. Refresh the page
2. The filter panel should now be visible
3. Test search and dietary filters functionality

### 4. Disable the feature toggle
```bash
curl -X PUT http://localhost:8080/features/menu-filter \
  -H "Content-Type: application/json" \
  -d "false"
```

## Key Files Modified ✅ ALL COMPLETED
1. `server/src/main/java/de/tum/aet/devops25/w10/controller/FeatureToggleController.java` - ✅ Both GET and PUT endpoints implemented
2. `client/src/routes/+page.ts` - ✅ Feature toggle fetch logic implemented with Promise.allSettled
3. `client/src/routes/+page.svelte` - ✅ Conditional filter UI rendering and filtering logic implemented

## Additional Implementation Details

### Backend Controller
- Uses `@RequestParam("enabled")` for the PUT endpoint parameter
- Implements proper error handling with `getOrDefault(featureName, false)`
- Stores feature toggles in a static HashMap for persistence during runtime

### Frontend Integration
- Uses `Promise.allSettled` to handle parallel API calls gracefully
- Provides fallback values when API calls fail
- Implements comprehensive filtering logic that respects the feature toggle state
- Shows/hides both the filter UI and applies filtering based on toggle state

## Notes
- The feature toggle state is stored in memory and will reset when the server restarts
- The frontend gracefully handles failed feature toggle requests by defaulting to `false`
- The `MealFilter` component is already imported and ready to use